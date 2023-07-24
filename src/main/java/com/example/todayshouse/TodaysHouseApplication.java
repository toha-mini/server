package com.example.todayshouse;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class TodaysHouseApplication {

    public static void main(String[] args) {
        SpringApplication.run(TodaysHouseApplication.class, args);
    }

}
