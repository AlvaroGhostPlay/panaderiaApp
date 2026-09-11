package com.example.authservice.token;

import com.example.authservice.oauht2.AuthenticatedUserPrincipal;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenCustomizer
        implements OAuth2TokenCustomizer<JwtEncodingContext> {

    @Override
    public void customize(JwtEncodingContext context) {

        Object principal =
                context
                        .getPrincipal()
                        .getPrincipal();

        if (!(principal instanceof AuthenticatedUserPrincipal user)) {
            return;
        }

        /*
         * ACCESS TOKEN
         */
        if (OAuth2TokenType.ACCESS_TOKEN
                .equals(context.getTokenType())) {

            context.getClaims()
                    .subject(
                            user.userId().toString()
                    )
                    .claim(
                            "username",
                            user.username()
                    )
                    .claim(
                            "roles",
                            user.roles()
                    );

            return;
        }

        /*
         * ID TOKEN
         *
         * El ID Token de OIDC no utiliza
         * OAuth2TokenType.ACCESS_TOKEN.
         *
         * Su valor es "id_token".
         */
        if ("id_token".equals(
                context.getTokenType().getValue()
        )) {

            context.getClaims()
                    .subject(
                            user.userId().toString()
                    )
                    .claim(
                            "username",
                            user.username()
                    )
                    .claim(
                            "roles",
                            user.roles()
                    );
        }
    }
}