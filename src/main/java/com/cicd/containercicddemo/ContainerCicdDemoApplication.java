package com.cicd.containercicddemo;

import com.cicd.containercicddemo.libs.Sha;
import com.cicd.containercicddemo.libs.ShaImpl;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class ContainerCicdDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(ContainerCicdDemoApplication.class, args);
    }

    @Bean
    public Sha sha() {
        return new ShaImpl();
    }
}
