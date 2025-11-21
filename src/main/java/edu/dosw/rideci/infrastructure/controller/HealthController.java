package edu.dosw.rideci.infrastructure.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/payments")
public class HealthController {

    @GetMapping("/ping")
    public String ping() {
        return "Payments service is alive!";
    }
}
