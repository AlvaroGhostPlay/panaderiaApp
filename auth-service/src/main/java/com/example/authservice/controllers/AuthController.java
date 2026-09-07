package com.example.authservice.controllers;

import com.example.authservice.dto.AuthenticateUserRequest;
import com.example.authservice.dto.LoginResponse;
import com.example.authservice.oauht2.AuthenticatedUserPrincipal;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import org.springframework.security.core.Authentication;

import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;

import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;

import org.springframework.security.web.context.SecurityContextRepository;

import org.springframework.security.web.csrf.CsrfToken;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {


    private final AuthenticationManager authenticationManager;

    private final SecurityContextRepository securityContextRepository;

    private final SessionAuthenticationStrategy sessionAuthenticationStrategy;


    /*
     * Angular consulta este endpoint antes del POST /login
     * para obtener el token CSRF.
     */
    @GetMapping("/csrf")
    public CsrfToken csrf(
            CsrfToken csrfToken
    ) {

        return csrfToken;
    }


    /*
     * Login personalizado consumido desde Angular.
     */
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(

            @RequestBody AuthenticateUserRequest loginRequest,

            HttpServletRequest request,

            HttpServletResponse response

    ) {

        try {

            /*
             * Todavía NO está autenticado.
             */
            UsernamePasswordAuthenticationToken authenticationRequest =
                    UsernamePasswordAuthenticationToken
                            .unauthenticated(
                                    loginRequest.username(),
                                    loginRequest.password()
                            );


            authenticationRequest.setDetails(
                    new WebAuthenticationDetailsSource()
                            .buildDetails(request)
            );


            /*
             * Llega a:
             *
             * RemoteAuthenticationProvider
             *      ↓
             * UsersServiceClient
             *      ↓
             * MSVC-USERS-ROLES
             */
            Authentication authentication =
                    authenticationManager.authenticate(
                            authenticationRequest
                    );


            AuthenticatedUserPrincipal principal =
                    (AuthenticatedUserPrincipal)
                            authentication.getPrincipal();


            /*
             * Si debe cambiar contraseña,
             * todavía NO creamos la sesión OAuth normal.
             */
            if (principal.passwordChangeRequired()) {

                return ResponseEntity
                        .status(HttpStatus.FORBIDDEN)
                        .body(
                                new LoginResponse(
                                        false,
                                        true,
                                        "PASSWORD_CHANGE_REQUIRED"
                                )
                        );
            }


            /*
             * Evita Session Fixation.
             */
            sessionAuthenticationStrategy
                    .onAuthentication(
                            authentication,
                            request,
                            response
                    );


            /*
             * Creamos el SecurityContext autenticado.
             */
            SecurityContext securityContext =
                    SecurityContextHolder
                            .createEmptyContext();


            securityContext.setAuthentication(
                    authentication
            );


            SecurityContextHolder.setContext(
                    securityContext
            );


            /*
             * Guardamos el usuario autenticado en HttpSession.
             *
             * Esto generará AUTHSESSION.
             */
            securityContextRepository
                    .saveContext(
                            securityContext,
                            request,
                            response
                    );


            return ResponseEntity.ok(
                    new LoginResponse(
                            true,
                            false,
                            null
                    )
            );


        } catch (BadCredentialsException exception) {

            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(
                            new LoginResponse(
                                    false,
                                    false,
                                    "INVALID_CREDENTIALS"
                            )
                    );


        } catch (DisabledException exception) {

            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body(
                            new LoginResponse(
                                    false,
                                    false,
                                    "ACCOUNT_DISABLED"
                            )
                    );


        } catch (AuthenticationServiceException exception) {

            return ResponseEntity
                    .status(HttpStatus.SERVICE_UNAVAILABLE)
                    .body(
                            new LoginResponse(
                                    false,
                                    false,
                                    "AUTH_SERVICE_UNAVAILABLE"
                            )
                    );
        }
    }
}