package com.example.RestSpringApp.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class DefaultController {

    @GetMapping()
    public String sayHello() {
        return "Hello world!";
    }
}

