package com.aevasquez.msvc.nadbar;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class MsvcNadbarApplication {

    public static void main(String[] args) {
        SpringApplication.run(MsvcNadbarApplication.class, args);
    }

}
