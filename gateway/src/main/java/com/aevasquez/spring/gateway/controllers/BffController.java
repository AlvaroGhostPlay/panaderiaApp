package com.aevasquez.spring.gateway.controllers;

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
    public Map<String, Object> me(

            @AuthenticationPrincipal
            OidcUser user

    ) {

        Map<String, Object> response =
                new LinkedHashMap<>();


        response.put(
                "authenticated",
                true
        );


        response.put(
                "sub",
                user.getSubject()
        );


        response.put(
                "name",
                user.getName()
        );


        response.put(
                "authorities",
                user.getAuthorities()
        );


        return response;
    }
}