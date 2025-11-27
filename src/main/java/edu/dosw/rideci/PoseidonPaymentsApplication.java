package edu.dosw.rideci;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "edu.dosw.rideci")
public class PoseidonPaymentsApplication {
    public static void main(String[] args) {
        SpringApplication.run(PoseidonPaymentsApplication.class, args);
    }
}
