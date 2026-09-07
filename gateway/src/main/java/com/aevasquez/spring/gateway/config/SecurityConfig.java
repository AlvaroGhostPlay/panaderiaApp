package com.aevasquez.spring.gateway.config;

import org.springframework.beans.factory.annotation.Value;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.http.HttpMethod;

import org.springframework.security.config.Customizer;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;

import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;

import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;

import org.springframework.security.oauth2.client.OAuth2AuthorizedClientProvider;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientProviderBuilder;

import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;

import org.springframework.security.oauth2.client.web.DefaultOAuth2AuthorizedClientManager;

import org.springframework.security.oauth2.client.web.HttpSessionOAuth2AuthorizedClientRepository;

import org.springframework.security.oauth2.client.web.OAuth2AuthorizedClientRepository;

import org.springframework.security.web.SecurityFilterChain;

import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {


    @Bean
    public OAuth2AuthorizedClientRepository
    authorizedClientRepository() {

        return new HttpSessionOAuth2AuthorizedClientRepository();
    }


    @Bean
    public OAuth2AuthorizedClientManager
    authorizedClientManager(

            ClientRegistrationRepository
                    clientRegistrationRepository,

            OAuth2AuthorizedClientRepository
                    authorizedClientRepository

    ) {


        OAuth2AuthorizedClientProvider provider =
                OAuth2AuthorizedClientProviderBuilder
                        .builder()

                        .authorizationCode()

                        .refreshToken()

                        .build();


        DefaultOAuth2AuthorizedClientManager manager =
                new DefaultOAuth2AuthorizedClientManager(
                        clientRegistrationRepository,
                        authorizedClientRepository
                );


        manager.setAuthorizedClientProvider(
                provider
        );


        return manager;
    }


    @Bean
    public CorsConfigurationSource corsConfigurationSource(

            @Value("${app.frontend-url}")
            String frontendUrl

    ) {

        CorsConfiguration configuration =
                new CorsConfiguration();


        configuration.setAllowedOrigins(
                List.of(
                        frontendUrl
                )
        );


        configuration.setAllowedMethods(
                List.of(
                        "GET",
                        "POST",
                        "PUT",
                        "DELETE",
                        "PATCH",
                        "OPTIONS"
                )
        );


        configuration.setAllowedHeaders(
                List.of("*")
        );


        configuration.setAllowCredentials(
                true
        );


        UrlBasedCorsConfigurationSource source =
                new UrlBasedCorsConfigurationSource();


        source.registerCorsConfiguration(
                "/**",
                configuration
        );


        return source;
    }


    @Bean
    public SecurityFilterChain securityFilterChain(

            HttpSecurity http,

            OAuth2AuthorizedClientRepository
                    authorizedClientRepository,

            @Value("${app.frontend-url}")
            String frontendUrl

    ) throws Exception {


        http.authorizeHttpRequests(
                authorize ->
                        authorize

                                .requestMatchers(
                                        "/oauth2/**",
                                        "/login/**",
                                        "/bff/csrf",
                                        "/actuator/health",
                                        "/error"
                                )
                                .permitAll()

                                .requestMatchers(
                                        HttpMethod.OPTIONS,
                                        "/**"
                                )
                                .permitAll()

                                .anyRequest()
                                .authenticated()
        );


        http.oauth2Login(
                oauth2 ->
                        oauth2

                                .authorizedClientRepository(
                                        authorizedClientRepository
                                )

                                .successHandler(
                                        (
                                                request,
                                                response,
                                                authentication
                                        ) ->

                                                response.sendRedirect(
                                                        frontendUrl
                                                )
                                )
        );


        http.oauth2Client(
                oauth2 ->
                        oauth2
                                .authorizedClientRepository(
                                        authorizedClientRepository
                                )
        );


        http.csrf(
                csrf ->
                        csrf.csrfTokenRepository(
                                CookieCsrfTokenRepository
                                        .withHttpOnlyFalse()
                        )
        );


        http.cors(
                Customizer.withDefaults()
        );


        http.formLogin(
                AbstractHttpConfigurer::disable
        );


        http.httpBasic(
                AbstractHttpConfigurer::disable
        );


        return http.build();
    }
}