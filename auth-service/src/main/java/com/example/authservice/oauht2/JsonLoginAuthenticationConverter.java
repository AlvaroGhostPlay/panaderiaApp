package com.example.authservice.oauht2;

import com.example.authservice.dto.AuthenticateUserRequest;
import tools.jackson.databind.json.JsonMapper;

import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationConverter;

import jakarta.servlet.http.HttpServletRequest;

public class JsonLoginAuthenticationConverter
        implements AuthenticationConverter {

    private final JsonMapper jsonMapper;

    public JsonLoginAuthenticationConverter(
            JsonMapper jsonMapper
    ) {
        this.jsonMapper = jsonMapper;
    }

    @Override
    public Authentication convert(
            HttpServletRequest request
    ) {

        try {

            AuthenticateUserRequest login =
                    jsonMapper.readValue(
                            request.getInputStream(),
                            AuthenticateUserRequest.class
                    );

            if (login.username() == null ||
                    login.password() == null) {

                throw new AuthenticationServiceException(
                        "Username y password son requeridos"
                );
            }

            return UsernamePasswordAuthenticationToken
                    .unauthenticated(
                            login.username(),
                            login.password()
                    );

        } catch (Exception exception) {

            throw new AuthenticationServiceException(
                    "Request de autenticación inválido",
                    exception
            );
        }
    }
}