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

//import org.springframework.web.cors.CorsConfiguration;
//import org.springframework.web.cors.CorsConfigurationSource;
//import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public AuthenticationManager authenticationManager(
            RemoteAuthenticationProvider remoteAuthenticationProvider
    ) {
        return new ProviderManager(remoteAuthenticationProvider);
    }

    @Bean
    public SecurityContextRepository securityContextRepository() {
        return new HttpSessionSecurityContextRepository();
    }

    @Bean
    public SessionAuthenticationStrategy sessionAuthenticationStrategy() {
        return new ChangeSessionIdAuthenticationStrategy();
    }

    @Bean
    public AuthenticationEntryPoint angularLoginEntryPoint(
            @Value("${app.frontend.login-url}") String loginUrl
    ) {
        return (request, response, exception) -> response.sendRedirect(loginUrl);
    }

   /* @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        configuration.setAllowedOrigins(
                List.of(
                        "http://panaderia.test:4200",
                        "http://localhost:4200",
                        "http://panaderia.test:8080",
                        "http://localhost:8080"
                )
        );

        configuration.setAllowedMethods(
                List.of("GET", "POST", "PUT", "DELETE", "OPTIONS")
        );

        configuration.setAllowedHeaders(
                List.of("Content-Type", "X-XSRF-TOKEN", "X-CSRF-TOKEN", "Authorization")
        );

        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }*/

    /*
     * =====================================================
     * CHAIN 1: Endpoints del Servidor de Autorización OAuth2/OIDC
     * =====================================================
     */
    @Bean
    @Order(1)
    public SecurityFilterChain authorizationServerSecurityFilterChain(
            HttpSecurity http,
            SecurityContextRepository securityContextRepository,
            AuthenticationEntryPoint angularLoginEntryPoint,
            RemoteAuthenticationProvider remoteAuthenticationProvider
    ) throws Exception {

        http
                // CORRECCIÓN CRÍTICA: Forzamos un securityMatcher exclusivo para los endpoints de OAuth2
                .securityMatcher(
                        "/oauth2/authorize",
                        "/oauth2/token",
                        "/oauth2/jwks",
                        "/oauth2/revocation",
                        "/oauth2/introspection",
                        "/.well-known/**",
                        "/userinfo",
                        "/connect/logout"
                )
                .oauth2AuthorizationServer(authorizationServer ->
                        authorizationServer.oidc(Customizer.withDefaults())
                )
                .authorizeHttpRequests(authorize ->
                        authorize.anyRequest().authenticated()
                )
                .authenticationProvider(remoteAuthenticationProvider)
                .securityContext(securityContext ->
                        securityContext.securityContextRepository(securityContextRepository)
                )
                .exceptionHandling(exceptions ->
                        exceptions.defaultAuthenticationEntryPointFor(
                                angularLoginEntryPoint,
                                new MediaTypeRequestMatcher(MediaType.TEXT_HTML)
                        )
                )
                //.cors(Customizer.withDefaults())
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable);

        return http.build();
    }

    /*
     * =====================================================
     * CHAIN 2: API de Autenticación personalizada (/api/auth/**)
     * =====================================================
     */
    @Bean
    @Order(2)
    public SecurityFilterChain applicationSecurityFilterChain(
            HttpSecurity http,
            RemoteAuthenticationProvider remoteAuthenticationProvider,
            SecurityContextRepository securityContextRepository
    ) throws Exception {

        http
                // CORRECCIÓN CRÍTICA: Delimitamos esta cadena exclusivamente para las peticiones API y raíz
                .securityMatcher("/api/**", "/actuator/**", "/error")
                .authenticationProvider(remoteAuthenticationProvider)
                .securityContext(securityContext ->
                        securityContext.securityContextRepository(securityContextRepository)
                )
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                )
                .csrf(csrf ->
                        csrf.ignoringRequestMatchers(
                                "/api/auth/login",
                                "/api/auth/csrf"
                        )
                )
                .authorizeHttpRequests(authorize ->
                        authorize
                                .requestMatchers(
                                        "/api/auth/login",
                                        "/api/auth/csrf",
                                        "/actuator/health",
                                        "/error"
                                ).permitAll()
                                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                                .anyRequest().authenticated()
                )
                //.cors(Customizer.withDefaults())
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable);

        return http.build();
    }
}