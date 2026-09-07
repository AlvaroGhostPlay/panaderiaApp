package com.example.authservice.config;

import com.example.authservice.oauht2.AuthenticatedUserPrincipal;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.jackson.SecurityJacksonModules;
import org.springframework.security.oauth2.server.authorization.JdbcOAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import tools.jackson.databind.json.JsonMapper;
import tools.jackson.databind.jsontype.BasicPolymorphicTypeValidator;

@Configuration
public class OAuth2PersistenceConfig {

    @Bean
    public OAuth2AuthorizationService authorizationService(
            JdbcTemplate jdbcTemplate,
            RegisteredClientRepository registeredClientRepository) {

        BasicPolymorphicTypeValidator.Builder typeValidator =
                BasicPolymorphicTypeValidator.builder()
                        .allowIfSubType(AuthenticatedUserPrincipal.class);

        JsonMapper jsonMapper = JsonMapper.builder()
                .addModules(
                        SecurityJacksonModules.getModules(
                                getClass().getClassLoader(),
                                typeValidator
                        )
                )
                .build();

        JdbcOAuth2AuthorizationService authorizationService =
                new JdbcOAuth2AuthorizationService(
                        jdbcTemplate,
                        registeredClientRepository
                );

        authorizationService.setAuthorizationRowMapper(
                new JdbcOAuth2AuthorizationService
                        .JsonMapperOAuth2AuthorizationRowMapper(
                        registeredClientRepository,
                        jsonMapper
                )
        );

        authorizationService.setAuthorizationParametersMapper(
                new JdbcOAuth2AuthorizationService
                        .JsonMapperOAuth2AuthorizationParametersMapper(
                        jsonMapper
                )
        );

        return authorizationService;
    }
}