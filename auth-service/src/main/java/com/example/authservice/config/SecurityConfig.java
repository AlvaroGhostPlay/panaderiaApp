package com.example.authservice.config;

import com.example.authservice.oauht2.RemoteAuthenticationProvider;

import org.springframework.beans.factory.annotation.Value;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.core.annotation.Order;

import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;

import org.springframework.security.config.Customizer;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;

import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;

import org.springframework.security.config.http.SessionCreationPolicy;

import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;

import org.springframework.security.web.authentication.session.ChangeSessionIdAuthenticationStrategy;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;

import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;

import org.springframework.security.web.util.matcher.MediaTypeRequestMatcher;

import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;


@Configuration
@EnableWebSecurity
public class SecurityConfig {


    /*
     * =====================================================
     * AuthenticationManager
     * =====================================================
     */

    @Bean
    public AuthenticationManager authenticationManager(
            RemoteAuthenticationProvider remoteAuthenticationProvider
    ) {

        return new ProviderManager(
                remoteAuthenticationProvider
        );
    }


    /*
     * =====================================================
     * SecurityContext guardado en HttpSession
     * =====================================================
     */

    @Bean
    public SecurityContextRepository securityContextRepository() {

        return new HttpSessionSecurityContextRepository();
    }


    /*
     * =====================================================
     * Protección contra Session Fixation
     * =====================================================
     */

    @Bean
    public SessionAuthenticationStrategy
    sessionAuthenticationStrategy() {

        return new ChangeSessionIdAuthenticationStrategy();
    }


    /*
     * =====================================================
     * Si alguien intenta /oauth2/authorize sin AUTHSESSION,
     * mandamos al login bonito de Angular.
     * =====================================================
     */

    @Bean
    public AuthenticationEntryPoint angularLoginEntryPoint(

            @Value("${app.frontend.login-url}")
            String loginUrl

    ) {

        return (request, response, exception) ->
                response.sendRedirect(loginUrl);
    }


    /*
     * =====================================================
     * CORS
     *
     * Angular :4200
     * Auth    :9000
     * =====================================================
     */

    @Bean
    public CorsConfigurationSource corsConfigurationSource(

            @Value("${app.frontend.origin}")
            String frontendOrigin

    ) {

        CorsConfiguration configuration =
                new CorsConfiguration();


        configuration.setAllowedOrigins(
                List.of(frontendOrigin)
        );


        configuration.setAllowedMethods(
                List.of(
                        "GET",
                        "POST",
                        "OPTIONS"
                )
        );


        configuration.setAllowedHeaders(
                List.of(
                        "Content-Type",
                        "X-CSRF-TOKEN",
                        "X-XSRF-TOKEN"
                )
        );


        /*
         * Necesario para enviar AUTHSESSION.
         */
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


    /*
     * =====================================================
     * CHAIN 1
     *
     * Endpoints OAuth2/OIDC:
     *
     * /oauth2/authorize
     * /oauth2/token
     * /oauth2/jwks
     * /.well-known/...
     * etc.
     * =====================================================
     */

    @Bean
    @Order(1)
    public SecurityFilterChain
    authorizationServerSecurityFilterChain(

            HttpSecurity http,

            SecurityContextRepository securityContextRepository,

            AuthenticationEntryPoint angularLoginEntryPoint

    ) throws Exception {


        http.oauth2AuthorizationServer(
                authorizationServer -> {

                    http.securityMatcher(
                            authorizationServer
                                    .getEndpointsMatcher()
                    );


                    authorizationServer
                            .oidc(
                                    Customizer.withDefaults()
                            );
                }
        );


        http.securityContext(
                securityContext ->
                        securityContext
                                .securityContextRepository(
                                        securityContextRepository
                                )
        );


        http.authorizeHttpRequests(
                authorize ->
                        authorize
                                .anyRequest()
                                .authenticated()
        );


        /*
         * Si /oauth2/authorize llega sin sesión,
         * enviamos al /login de Angular.
         */
        http.exceptionHandling(
                exceptions ->
                        exceptions
                                .defaultAuthenticationEntryPointFor(

                                        angularLoginEntryPoint,

                                        new MediaTypeRequestMatcher(
                                                MediaType.TEXT_HTML
                                        )
                                )
        );


        http.formLogin(
                AbstractHttpConfigurer::disable
        );


        http.httpBasic(
                AbstractHttpConfigurer::disable
        );


        return http.build();
    }


    /*
     * =====================================================
     * CHAIN 2
     *
     * Nuestro endpoint personalizado de login.
     * =====================================================
     */

    @Bean
    @Order(2)
    public SecurityFilterChain
    applicationSecurityFilterChain(

            HttpSecurity http,

            RemoteAuthenticationProvider remoteAuthenticationProvider,

            SecurityContextRepository securityContextRepository

    ) throws Exception {


        http.authenticationProvider(
                remoteAuthenticationProvider
        );


        http.securityContext(
                securityContext ->
                        securityContext
                                .securityContextRepository(
                                        securityContextRepository
                                )
        );


        http.sessionManagement(
                session ->
                        session.sessionCreationPolicy(
                                SessionCreationPolicy.IF_REQUIRED
                        )
        );


        http.authorizeHttpRequests(
                authorize ->
                        authorize

                                .requestMatchers(
                                        "/api/auth/login",
                                        "/api/auth/csrf",
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


        http.cors(
                Customizer.withDefaults()
        );


        /*
         * NO utilizamos el formLogin de Spring.
         */
        http.formLogin(
                AbstractHttpConfigurer::disable
        );


        /*
         * Tampoco HTTP Basic para usuarios.
         */
        http.httpBasic(
                AbstractHttpConfigurer::disable
        );


        /*
         * IMPORTANTE:
         *
         * NO hacemos:
         *
         * csrf.disable()
         *
         * CSRF queda habilitado.
         */


        return http.build();
    }
}