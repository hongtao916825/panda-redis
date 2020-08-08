package com.panda.redis.springexample;

import com.panda.redis.core.autoconfigure.EnablePandaRedis;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnablePandaRedis
public class SpringExampleApplication {

    public static void main(String[] args) {
        try {
            SpringApplication.run(SpringExampleApplication.class, args);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
