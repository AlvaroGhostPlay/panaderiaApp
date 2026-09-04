package com.aevasquez.images.service.controllers;

import com.aevasquez.images.service.ImagesRequest;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
public class FileController {
    private final String PUBLIC_DIR = "src/main/resources/templates/uploads/public/";
    private final String PRIVATE_DIR = "uploads/private/";

    @PostMapping("/upload")
    public ResponseEntity<?> uploadImage(
            @RequestParam("file") MultipartFile file,
            @RequestParam("isPublic") boolean isPublic) throws IOException {

        String directory = isPublic ? PUBLIC_DIR : PRIVATE_DIR;
        Path filePath = Paths.get(directory + file.getOriginalFilename());

        // Crea los directorios si no existen
        Files.createDirectories(filePath.getParent());

        // Guarda el archivo en el disco
        Files.write(filePath, file.getBytes());

        return ResponseEntity.ok("Archivo subido con éxito: " + file.getOriginalFilename());
    }

    @GetMapping("/private/images")
    public ResponseEntity<?> getPrivateImage(
            @RequestParam String filename,
            @RequestHeader("Authorization") String token) {

        // 2. Si tiene permisos, le devuelves la imagen
        try {
            Path filePath = Paths.get("uploads/private/" + filename);
            Resource resource = new UrlResource(filePath.toUri());

            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_JPEG) // O detectarlo dinámicamente
                    .body(resource);
        } catch (MalformedURLException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/public/images")
    public ResponseEntity<?> getpubliImage(
            @RequestParam String filename) {

        try {
            Path filePath = Paths.get(PUBLIC_DIR + filename);
            Resource resource = new UrlResource(filePath.toUri());

            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_JPEG) // O detectarlo dinámicamente
                    .body(resource);
        } catch (MalformedURLException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/public/images")
    public ResponseEntity<?> getPublicImagesByname(@RequestBody ImagesRequest request) {

        // Retornamos un Mapa con el formato: { "nombre_imagen.jpg": "data:image/jpeg;base64,xxxx..." }
        Map<String, String> imagenesBase64 = request.images().stream()
                .collect(Collectors.toMap(
                        imageName -> imageName, // La clave será el nombre del archivo
                        imageName -> {
                            try {
                                Path filePath = Paths.get(PUBLIC_DIR, imageName);
                                if (!Files.exists(filePath)) {
                                    return ""; // O manejar una imagen por defecto si no existe
                                }
                                byte[] fileContent = Files.readAllBytes(filePath);
                                String base64 = Base64.getEncoder().encodeToString(fileContent);

                                // Detectar el tipo de contenido dinámicamente si es posible, o hardcodear jpeg/png
                                String contentType = imageName.endsWith(".png") ? "image/png" : "image/jpeg";

                                return "data:" + contentType + ";base64," + base64;
                            } catch (IOException e) {
                                return "";
                            }
                        }
                ));

        return ResponseEntity.ok(imagenesBase64);
    }
}
