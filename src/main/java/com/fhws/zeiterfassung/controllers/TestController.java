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
        String someString = "Hello from the backend";
        return new ResponseEntity<>(someString, HttpStatus.OK);
    }
}
