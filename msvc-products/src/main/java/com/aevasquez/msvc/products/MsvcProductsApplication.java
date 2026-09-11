package com.aevasquez.msvc.products;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class MsvcProductsApplication {

    public static void main(String[] args) {
        SpringApplication.run(MsvcProductsApplication.class, args);
    }

}
