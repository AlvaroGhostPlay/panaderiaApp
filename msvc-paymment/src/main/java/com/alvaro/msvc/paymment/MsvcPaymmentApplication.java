package com.alvaro.msvc.paymment;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class MsvcPaymmentApplication {

    public static void main(String[] args) {
        SpringApplication.run(MsvcPaymmentApplication.class, args);
    }

}
