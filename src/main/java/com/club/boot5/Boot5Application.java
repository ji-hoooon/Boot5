package com.club.boot5;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
//BaseEntity 리스너 사용을 위한
@EnableJpaAuditing
public class Boot5Application {

    public static void main(String[] args) {
        SpringApplication.run(Boot5Application.class, args);
    }

}
