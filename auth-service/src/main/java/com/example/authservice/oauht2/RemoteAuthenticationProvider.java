package com.example.authservice.oauht2;

import com.example.authservice.dto.AuthenticatedUserResponse;
import com.example.authservice.services.UsersServiceClient;

import lombok.RequiredArgsConstructor;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;

import org.springframework.security.core.authority.FactorGrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class RemoteAuthenticationProvider implements AuthenticationProvider {

    private final UsersServiceClient usersServiceClient;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();

        String password =
                authentication.getCredentials().toString();

        AuthenticatedUserResponse response =
                usersServiceClient.authenticate(
                        username,
                        password
                );

        if (response == null || !response.authenticated()) {
            throw new BadCredentialsException(
                    "Invalid username or password"
            );
        }

        if (!response.enabled()) {
            throw new DisabledException(
                    "User account is disabled"
            );
        }

        Set<GrantedAuthority> authorities =
                new HashSet<>();

        response.roles().forEach(role -> {

            String authority =
                    role.startsWith("ROLE_")
                            ? role
                            : "ROLE_" + role;

            authorities.add(
                    new SimpleGrantedAuthority(
                            authority
                    )
            );
            authorities.add(
                    FactorGrantedAuthority.fromAuthority(
                            FactorGrantedAuthority.PASSWORD_AUTHORITY
                    )
            );
        });

        AuthenticatedUserPrincipal principal =
                new AuthenticatedUserPrincipal(
                        response.userId(),
                        response.username(),
                        response.changePass(),
                        response.roles()
                );

        return UsernamePasswordAuthenticationToken
                .authenticated(
                        principal,
                        null,
                        authorities
                );
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class
                .isAssignableFrom(authentication);
    }
}
