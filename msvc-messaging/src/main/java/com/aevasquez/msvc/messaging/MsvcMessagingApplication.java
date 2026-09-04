package com.aevasquez.msvc.messaging;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class MsvcMessagingApplication {

    public static void main(String[] args) {
        SpringApplication.run(MsvcMessagingApplication.class, args);
    }

}
