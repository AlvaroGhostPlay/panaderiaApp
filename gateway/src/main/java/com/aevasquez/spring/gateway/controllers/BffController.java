package com.aevasquez.spring.gateway.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import org.springframework.security.oauth2.core.oidc.user.OidcUser;

import org.springframework.security.web.csrf.CsrfToken;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping("/bff")
public class BffController {


    @GetMapping("/csrf")
    public CsrfToken csrf(
            CsrfToken csrfToken
    ) {

        return csrfToken;
    }


    @GetMapping("/me")
    public ResponseEntity<?> me(@AuthenticationPrincipal OidcUser user) {

        Map<String, Object> response =
                new LinkedHashMap<>();

        if (user == null) {
            return ResponseEntity.status(HttpStatus.OK).body(Map.of("authenticated", false));
        }

        response.put("authenticated", true);
        response.put("sub", user.getSubject());
        response.put("name", user.getName());
        response.put("authorities", user.getAuthorities());
        response.put("roles", user.getClaimAsStringList("roles"));
        return ResponseEntity.ok(response);
    }
}