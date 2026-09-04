package com.aevasquez.msvc.clients;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class MsvcClientsApplication {

    public static void main(String[] args) {
        SpringApplication.run(MsvcClientsApplication.class, args);
    }

}
