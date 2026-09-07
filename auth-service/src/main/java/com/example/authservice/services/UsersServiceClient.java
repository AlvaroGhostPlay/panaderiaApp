package com.example.authservice.services;

import com.example.authservice.dto.AuthenticateUserRequest;
import com.example.authservice.dto.AuthenticatedUserResponse;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class UsersServiceClient {

    private final RestTemplate loadBalancedRestTemplate;

    @Value("${services.users.url}")
    private String usersServiceUrl;

    public AuthenticatedUserResponse authenticate(
            String username,
            String password
    ) {

        AuthenticateUserRequest request =
                new AuthenticateUserRequest(
                        username,
                        password
                );

        return loadBalancedRestTemplate.postForObject(
                usersServiceUrl + "/user/auth/user",
                request,
                AuthenticatedUserResponse.class
        );
    }
}