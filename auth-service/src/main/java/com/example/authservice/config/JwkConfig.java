package com.example.authservice.config;

import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import org.springframework.security.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.oauth2.jwt.JwtDecoder;

import java.io.InputStream;
import java.security.Key;
import java.security.KeyStore;
import java.security.cert.Certificate;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

@Configuration
public class JwkConfig {

    @Bean
    public JWKSource<SecurityContext> jwkSource(
            @Value("${auth.jwt.key-store}") Resource keyStoreResource,
            @Value("${auth.jwt.key-store-password}") String keyStorePassword,
            @Value("${auth.jwt.key-alias}") String keyAlias,
            @Value("${auth.jwt.key-password}") String keyPassword,
            @Value("${auth.jwt.key-id}") String keyId
    ) {

        try {

            KeyStore keyStore = KeyStore.getInstance("PKCS12");

            try (InputStream inputStream =
                         keyStoreResource.getInputStream()) {

                keyStore.load(
                        inputStream,
                        keyStorePassword.toCharArray()
                );
            }

            Key key = keyStore.getKey(
                    keyAlias,
                    keyPassword.toCharArray()
            );

            if (!(key instanceof RSAPrivateKey privateKey)) {
                throw new IllegalStateException(
                        "La clave privada configurada no es RSA"
                );
            }

            Certificate certificate =
                    keyStore.getCertificate(keyAlias);

            if (certificate == null) {
                throw new IllegalStateException(
                        "No se encontró el certificado para el alias: "
                                + keyAlias
                );
            }

            if (!(certificate.getPublicKey()
                    instanceof RSAPublicKey publicKey)) {

                throw new IllegalStateException(
                        "La clave pública configurada no es RSA"
                );
            }

            RSAKey rsaKey =
                    new RSAKey.Builder(publicKey)
                            .privateKey(privateKey)
                            .keyID(keyId)
                            .build();

            return new ImmutableJWKSet<>(
                    new JWKSet(rsaKey)
            );

        } catch (Exception exception) {

            throw new IllegalStateException(
                    "No se pudieron cargar las claves JWT",
                    exception
            );
        }
    }


    @Bean
    public JwtDecoder jwtDecoder(
            JWKSource<SecurityContext> jwkSource
    ) {

        return OAuth2AuthorizationServerConfiguration
                .jwtDecoder(jwkSource);
    }
}