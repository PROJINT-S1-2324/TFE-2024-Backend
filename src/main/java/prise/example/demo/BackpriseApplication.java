package prise.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class BackpriseApplication {

    public static void main(String[] args) {
        SpringApplication.run(BackpriseApplication.class, args);
    }
}
