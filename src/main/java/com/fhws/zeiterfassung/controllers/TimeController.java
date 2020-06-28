package com.fhws.zeiterfassung.controllers;

import com.fhws.zeiterfassung.models.TimeViewModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
public class TimeController {

    @RequestMapping(value = "/add-times", method = RequestMethod.POST)
    public ResponseEntity<?> addTimes(@RequestBody ArrayList<TimeViewModel> timeViewModels, @RequestHeader String authorization) {
        return new ResponseEntity<>(HttpStatus.OK);
    }

    public ResponseEntity<?> getTimes(@RequestHeader String authorization) {
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
