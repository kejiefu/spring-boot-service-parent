package com.mountain.admin;


import de.codecentric.boot.admin.server.config.EnableAdminServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@Slf4j
@EnableAdminServer
/**
 * http://localhost:8000/
 */
public class Application {

    public static void main(String[] args) {
        log.info("~~~~~~~~~~~~~~~~ program start~~~~~~~~~~~~~~~~!");
        SpringApplication.run(Application.class, args);
        log.info("~~~~~~~~~~~~~~~~ program execute successfully~~~~~~~~~~~~~~~~!");
    }
}
