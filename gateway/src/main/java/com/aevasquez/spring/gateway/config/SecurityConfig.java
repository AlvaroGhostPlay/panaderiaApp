package com.aevasquez.spring.gateway.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientProvider;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientProviderBuilder;
import org.springframework.security.oauth2.client.oidc.web.logout.OidcClientInitiatedLogoutSuccessHandler;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.DefaultOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.web.HttpSessionOAuth2AuthorizedClientRepository;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizedClientRepository;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.server.csrf.CsrfToken;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public OAuth2AuthorizedClientRepository authorizedClientRepository() {
        return new HttpSessionOAuth2AuthorizedClientRepository();
    }

    @Bean
    public OAuth2AuthorizedClientManager authorizedClientManager(
            ClientRegistrationRepository clientRegistrationRepository,
            OAuth2AuthorizedClientRepository authorizedClientRepository) {

        OAuth2AuthorizedClientProvider provider =
                OAuth2AuthorizedClientProviderBuilder.builder()
                        .authorizationCode()
                        .refreshToken()
                        .build();

        DefaultOAuth2AuthorizedClientManager manager =
                new DefaultOAuth2AuthorizedClientManager(
                        clientRegistrationRepository,
                        authorizedClientRepository
                );

        manager.setAuthorizedClientProvider(provider);

        return manager;
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource(
            @Value("${app.frontend-url}") String frontendUrl) {

        CorsConfiguration configuration = new CorsConfiguration();

        configuration.setAllowedOrigins(
                List.of(frontendUrl)
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

        configuration.setAllowCredentials(true);

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
            OAuth2AuthorizedClientRepository authorizedClientRepository,
            ClientRegistrationRepository clientRegistrationRepository,
            @Value("${app.frontend-url}") String frontendUrl
    ) throws Exception {

        OidcClientInitiatedLogoutSuccessHandler oidcLogoutSuccessHandler =
                new OidcClientInitiatedLogoutSuccessHandler(
                        clientRegistrationRepository
                );

        oidcLogoutSuccessHandler.setPostLogoutRedirectUri(
                "{baseUrl}/"
        );

        http

                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(
                                new HttpStatusEntryPoint(
                                        HttpStatus.UNAUTHORIZED
                                )
                        )
                )

                .authorizeHttpRequests(authorize -> authorize

                        .requestMatchers(
                                "/oauth2/**",
                                "/login/**",

                                // CSRF
                                "/bff/csrf",

                                // BFF
                                "/bff/me",

                                "/actuator/health",
                                "/error",

                                // Login propio
                                "/api/auth/csrf",
                                "/api/auth/login",

                                // Públicos
                                "/api/v1/contents/**",
                                "/api/v1/images/public/**",
                                "/api/v1/products/public/**",

                                "/logout/**"
                        ).permitAll()

                        .requestMatchers(
                                "/api/v1/products/**",
                                "/api/v1/paymment/**"
                        ).authenticated()

                        .requestMatchers(
                                HttpMethod.OPTIONS,
                                "/**"
                        ).permitAll()

                        .anyRequest().authenticated()
                )

                /*.oauth2Login(oauth2 -> oauth2
                        .authorizedClientRepository(
                                authorizedClientRepository
                        )
                        .successHandler(
                                (request, response, authentication) ->
                                        response.sendRedirect(
                                                frontendUrl + "/rollback"
                                        )
                        )
                )*/

                .oauth2Login(oauth2 -> oauth2
                        .authorizedClientRepository(authorizedClientRepository)
                        .successHandler((request, response, authentication) -> {

                            CsrfToken csrfToken = (CsrfToken) request.getAttribute(
                                    CsrfToken.class.getName()
                            );

                            response.sendRedirect(
                                    frontendUrl + "/rollback"
                            );
                        })
                )

                .oauth2Client(oauth2 -> oauth2
                        .authorizedClientRepository(
                                authorizedClientRepository
                        )
                )

                .logout(logout -> logout
                        .logoutSuccessHandler(
                                oidcLogoutSuccessHandler
                        )
                )

                /*
                 * =====================================================
                 * CSRF
                 * =====================================================
                 *
                 * El login inicial no puede exigir un token que
                 * todavía no hemos obtenido.
                 *
                 * Por eso /api/auth/login y /api/auth/csrf se ignoran.
                 */

                .csrf(csrf -> csrf
                        .csrfTokenRepository(
                                CookieCsrfTokenRepository.withHttpOnlyFalse()
                        )
                        .csrfTokenRequestHandler(
                                new SpaCsrfTokenRequestHandler()
                        )
                        .ignoringRequestMatchers(
                                "/api/auth/login",
                                "/api/auth/csrf",
                                "/api/v1/images/public/**"
                        )
                )

                .cors(Customizer.withDefaults())

                .formLogin(AbstractHttpConfigurer::disable)

                .httpBasic(AbstractHttpConfigurer::disable);

        return http.build();
    }
}