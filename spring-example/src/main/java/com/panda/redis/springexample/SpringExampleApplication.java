package com.panda.redis.springexample;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SpringExampleApplication {

    public static void main(String[] args) {
        try {
            SpringApplication.run(SpringExampleApplication.class, args);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
