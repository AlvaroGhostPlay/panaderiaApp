package com.aevasquez.msvc.clients.services;

import com.aevasquez.msvc.clients.dto.ClientRequest;
import com.aevasquez.msvc.clients.dto.ClientResponseDto;
import com.aevasquez.msvc.clients.dto.EmailRequest;
import com.aevasquez.msvc.clients.dto.UserCreateDto;
import com.aevasquez.msvc.clients.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class UserServiceImpl {

    @Autowired
    private UserService userService;

    @Autowired
    private EmailServiceImpl emailServiceImpl;

    @Autowired
    private EmailService emailService;

    public User createUser(ClientRequest cliente, ClientResponseDto clientResponseDto){
        UserCreateDto user = createUser(clientResponseDto, clientResponseDto.clienteNatural(), cliente.password());
        User userResponse = userService.createUser(user);
        EmailRequest request = emailServiceImpl.createEmailReuqestSendWelcome(
                clientResponseDto.email(),
                "¡Bienvenido a la aplicación!",
                "bienvenida",
                clientResponseDto.name(),
                userResponse.username(),
                clientResponseDto.email(),
                /*userResponse.actiovationCode()*/
                "121212121212",
                "www.youtube.com"
                );

        emailService.sendEmailWelcome(request);
        return userResponse;
    }

    private UserCreateDto createUser(ClientResponseDto client, ClientResponseDto.ClienteNatural clientNatural, String password) {
        String username = "";

        // CORRECCIÓN: Si clientType() ya es un String, compara con equals. Si es Enum, usa .name() en ambos lados.
        boolean isNatural = client.clientType().toString().equals(com.aevasquez.msvc.clients.dto.ClientType.N.name());

        if (isNatural && clientNatural != null) {
            // Aseguramos valores por defecto si vienen nulos en el DTO
            String firstName = clientNatural.firstName() != null ? clientNatural.firstName() : "XX";
            String secondName = clientNatural.secondName() != null ? clientNatural.secondName() : "XX";
            String firstLastname = clientNatural.firstLastname() != null ? clientNatural.firstLastname() : "XXXX";

            // Validamos longitudes mínimas para evitar IndexOutOfBoundsException
            String p1 = firstName.length() >= 2 ? firstName.substring(0, 2) : firstName;
            String p2 = secondName.length() >= 2 ? secondName.substring(0, 2) : secondName;
            String p3 = firstLastname.length() >= 4 ? firstLastname.substring(0, 4) : firstLastname;

            username = (p1 + p2 + p3).toUpperCase() + ThreadLocalRandom.current().nextInt(100, 1000);
        } else {
            // Nombre de usuario por defecto en caso de ser Persona Jurídica / Legal
            username = "CLI-" + client.clientId().toString().substring(0, 5).toUpperCase()
                    + ThreadLocalRandom.current().nextInt(100, 1000);
        }

        return new UserCreateDto(
                client.clientId(),
                username,
                password, // Recomiendo no mandar null en contraseñas obligatorias
                new Date(),
                new Date(),
                true,
                false,
                null);
    }
}
