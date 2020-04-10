package com.fhws.zeiterfassung.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
public class TestController {

    @GetMapping("/testApiCall")
    public ResponseEntity<String> testApiCall() {
        return new ResponseEntity<>("Hello from the backend", HttpStatus.OK);
    }
}
