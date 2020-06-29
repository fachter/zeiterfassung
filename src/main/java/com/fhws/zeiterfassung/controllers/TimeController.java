package com.fhws.zeiterfassung.controllers;

import com.fhws.zeiterfassung.boundaries.GetUsersTime;
import com.fhws.zeiterfassung.boundaries.SaveUsersTime;
import com.fhws.zeiterfassung.models.WorkedTimeViewModel;
import com.fhws.zeiterfassung.utils.LoggedInUserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
public class TimeController {

    private final LoggedInUserUtil loggedInUserUtil;
    private final GetUsersTime getUsersTime;
    private final SaveUsersTime saveUsersTime;

    @Autowired
    public TimeController(LoggedInUserUtil loggedInUserUtil,
                          GetUsersTime getUsersTime,
                          SaveUsersTime saveUsersTime) {
        this.loggedInUserUtil = loggedInUserUtil;
        this.getUsersTime = getUsersTime;
        this.saveUsersTime = saveUsersTime;
    }

    @RequestMapping(value = "/add-times", method = RequestMethod.POST)
    public ResponseEntity<?> addTimes(@RequestBody ArrayList<WorkedTimeViewModel> workedTimeViewModels, @RequestHeader String authorization) {
        return new ResponseEntity<>(HttpStatus.OK);
    }

    public ResponseEntity<?> getTimes(@RequestHeader String authorization) {
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
