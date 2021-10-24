package com.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@ComponentScan(basePackages = "com.flight")
public class FlightPriceApplication {

    public static void main(String[] args) {
        SpringApplication.run(FlightPriceApplication.class, args);
    }

}
