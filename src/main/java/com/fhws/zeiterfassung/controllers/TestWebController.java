package com.fhws.zeiterfassung.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class TestWebController {

    @RequestMapping("/")
    public String test() {
        return "This shit is really working!";
    }
}
