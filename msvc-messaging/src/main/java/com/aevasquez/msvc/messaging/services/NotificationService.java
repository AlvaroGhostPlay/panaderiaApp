package com.aevasquez.msvc.messaging.services;

import com.aevasquez.msvc.messaging.dto.EmailRequest;
import com.aevasquez.msvc.messaging.dto.ImageUploadResponse;
import com.aevasquez.msvc.messaging.publisher.EmailPublisher;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.List;
import java.util.Map;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.springframework.cloud.client.discovery.DiscoveryClient;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final JavaMailSender mailSender;
    private final EmailPublisher emailPublisher;
    //private final SpringTemplateEngine templateEngine; // Motor de Thymeleaf
    //private final ImageService imageService;
    private final DiscoveryClient discoveryClient;

    // 1. Método para la API REST (lo llama el Controller para enviar a RabbitMQ)
    public void sendEmail(EmailRequest message) {
        emailPublisher.publish(message);
    }

    public void sendRealEmail(EmailRequest request) {
        try {

            /*MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

            // 1. Cargar las variables dinámicas en el Contexto de Thymeleaf
            Context context = new Context();
            if (request.data() != null) {
                context.setVariables(request.data());
            }

            // 2. Procesar la plantilla HTML dinámicamente
            // Si request.template() es "bienvenida", cargará "templates/email/bienvenida.html"
            String htmlContent = templateEngine.process("email/" + request.template(), context);

            // 3. Configurar datos del correo
            helper.setTo(request.to());
            helper.setSubject(request.subject());
            helper.setText(htmlContent, true); // 'true' habilita la renderización de HTML

            mailSender.send(mimeMessage);*/
            List<ServiceInstance> instances = discoveryClient.getInstances("images-service");
            String imageUrl = "";
            if (instances != null && !instances.isEmpty()) {
                // Tomamos la primera instancia disponible (Eureka nos da su IP y su puerto dinámico)
                ServiceInstance instance = instances.get(0);

                imageUrl = instance.getUri().toString() + "/public/images?filename=logo-pequeno.png";
            } else {
                throw new RuntimeException("No se encontró el servicio de imágenes en Eureka");
            }

            request.data().put("logoUrl", imageUrl);

            String htmlContent = this.processXsltTemplate(request.template(), request.data());

            // 2. Preparar y enviar el correo HTML
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

            helper.setTo(request.to());
            helper.setSubject(request.subject());
            helper.setText(htmlContent, true);
            System.out.println("HTML GENERADO: " + htmlContent);

            mailSender.send(mimeMessage);

        } catch (MessagingException e) {
            throw new RuntimeException("Error al procesar o enviar el correo HTML", e);
        } catch (Exception e) {
            throw new RuntimeException("Error al procesar o enviar el correo XSL", e);
        }
    }

    private String processXsltTemplate(String templateName, Map<String, Object> data) throws Exception {
        // Generar un XML DOM en memoria a partir del Map <data>
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
        Document doc = docBuilder.newDocument();

        Element rootElement = doc.createElement("data");
        doc.appendChild(rootElement);

        if (data != null) {
            for (Map.Entry<String, Object> entry : data.entrySet()) {
                Element element = doc.createElement(entry.getKey());
                element.setTextContent(entry.getValue() != null ? entry.getValue().toString() : "");
                rootElement.appendChild(element);
            }
        }

        // Cargar el archivo .xsl desde la carpeta /templates/email/
        ClassPathResource resource = new ClassPathResource("templates/email/" + templateName + ".xsl");

        try (InputStream xslStream = resource.getInputStream()) {
            TransformerFactory factory = TransformerFactory.newInstance();
            Transformer transformer = factory.newTransformer(new StreamSource(xslStream));

            StringWriter writer = new StringWriter();
            transformer.transform(new DOMSource(doc), new StreamResult(writer));

            return writer.toString();
        }
    }
}
