package com.example.authservice.config;

import org.springframework.beans.factory.annotation.Value;

import org.springframework.boot.ApplicationRunner;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;

import org.springframework.security.oauth2.core.oidc.OidcScopes;

import org.springframework.security.oauth2.server.authorization.client.JdbcRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;

import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;

import java.time.Duration;
import java.util.UUID;


@Configuration
public class RegisteredClientConfig {

    @Bean
    public RegisteredClientRepository registeredClientRepository(
            JdbcTemplate jdbcTemplate) {

        return new JdbcRegisteredClientRepository(jdbcTemplate);
    }


    @Bean
    public PasswordEncoder oauthClientPasswordEncoder() {

        return PasswordEncoderFactories
                .createDelegatingPasswordEncoder();
    }


    @Bean
    public ApplicationRunner registerGatewayClient(

            RegisteredClientRepository registeredClientRepository,

            PasswordEncoder passwordEncoder,

            @Value("${auth.clients.gateway.client-id}")
            String clientId,

            @Value("${auth.clients.gateway.client-secret}")
            String clientSecret,

            @Value("${auth.clients.gateway.redirect-uri}")
            String redirectUri

    ) {

        return args -> {


            RegisteredClient existingClient =
                    registeredClientRepository
                            .findByClientId(
                                    clientId
                            );


            /*
             * Si ya existe, no lo volvemos a insertar.
             */
            if (existingClient != null) {
                return;
            }


            RegisteredClient gatewayClient =
                    RegisteredClient
                            .withId(
                                    UUID.randomUUID()
                                            .toString()
                            )

                            .clientId(
                                    clientId
                            )

                            /*
                             * La contraseña del cliente
                             * debe guardarse ENCODED.
                             */
                            .clientSecret(
                                    passwordEncoder.encode(
                                            clientSecret
                                    )
                            )

                            .clientName(
                                    "Gateway BFF"
                            )

                            .clientAuthenticationMethod(
                                    ClientAuthenticationMethod
                                            .CLIENT_SECRET_BASIC
                            )

                            .authorizationGrantType(
                                    AuthorizationGrantType
                                            .AUTHORIZATION_CODE
                            )

                            .authorizationGrantType(
                                    AuthorizationGrantType
                                            .REFRESH_TOKEN
                            )

                            .redirectUri(
                                    redirectUri
                            )

                            .scope(
                                    OidcScopes.OPENID
                            )

                            .scope(
                                    OidcScopes.PROFILE
                            )

                            .scope(
                                    "api"
                            )

                            .clientSettings(
                                    ClientSettings
                                            .builder()

                                            .requireProofKey(
                                                    true
                                            )

                                            .requireAuthorizationConsent(
                                                    false
                                            )

                                            .build()
                            )

                            .tokenSettings(
                                    TokenSettings
                                            .builder()

                                            .accessTokenTimeToLive(
                                                    Duration
                                                            .ofMinutes(10)
                                            )

                                            .refreshTokenTimeToLive(
                                                    Duration
                                                            .ofHours(8)
                                            )

                                            .reuseRefreshTokens(
                                                    false
                                            )

                                            .build()
                            )

                            .build();


            registeredClientRepository.save(
                    gatewayClient
            );
        };
    }
}