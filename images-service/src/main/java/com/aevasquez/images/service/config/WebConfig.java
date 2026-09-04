package com.aevasquez.images.service.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Todo lo que entre a /public/images/ buscará en la carpeta local "uploads/public/"
        registry.addResourceHandler("/public/images/**")
                .addResourceLocations("file:uploads/public/");
    }
}
