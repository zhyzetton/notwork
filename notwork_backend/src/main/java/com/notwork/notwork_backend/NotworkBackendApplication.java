package com.notwork.notwork_backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class NotworkBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(NotworkBackendApplication.class, args);
    }

}
