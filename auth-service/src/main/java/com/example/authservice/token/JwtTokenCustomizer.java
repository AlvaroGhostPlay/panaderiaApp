package com.example.authservice.token;

import com.example.authservice.oauht2.AuthenticatedUserPrincipal;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenCustomizer  implements OAuth2TokenCustomizer<JwtEncodingContext> {

    @Override
    public void customize(JwtEncodingContext context) {
        if (!OAuth2TokenType.ACCESS_TOKEN
                .equals(context.getTokenType())) {
            return;
        }

        Object principal =
                context
                        .getPrincipal()
                        .getPrincipal();

        if (!(principal instanceof
                AuthenticatedUserPrincipal user)) {
            return;
        }

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
