package com.aevasquez.spring.gateway.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;
import org.springframework.security.web.csrf.CsrfTokenRequestHandler;
import org.springframework.security.web.csrf.XorCsrfTokenRequestAttributeHandler;
import org.springframework.util.StringUtils;

import java.util.function.Supplier;

public final class SpaCsrfTokenRequestHandler
        implements CsrfTokenRequestHandler {

    private final CsrfTokenRequestHandler plain =
            new CsrfTokenRequestAttributeHandler();

    private final CsrfTokenRequestHandler xor =
            new XorCsrfTokenRequestAttributeHandler();

    @Override
    public void handle(
            HttpServletRequest request,
            HttpServletResponse response,
            Supplier<CsrfToken> csrfToken
    ) {
        // Mantiene la protección BREACH de Spring
        this.xor.handle(request, response, csrfToken);

        // Fuerza a generar/refrescar XSRF-TOKEN en la cookie
        csrfToken.get();
    }

    @Override
    public String resolveCsrfTokenValue(
            HttpServletRequest request,
            CsrfToken csrfToken
    ) {
        String headerValue =
                request.getHeader(csrfToken.getHeaderName());

        /*
         * Angular manda el token RAW que obtuvo de XSRF-TOKEN.
         * Por eso usamos el handler plain cuando viene
         * X-XSRF-TOKEN.
         */
        if (StringUtils.hasText(headerValue)) {
            return this.plain.resolveCsrfTokenValue(
                    request,
                    csrfToken
            );
        }

        return this.xor.resolveCsrfTokenValue(
                request,
                csrfToken
        );
    }
}