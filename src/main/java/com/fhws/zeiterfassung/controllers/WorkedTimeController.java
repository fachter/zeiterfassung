package com.fhws.zeiterfassung.controllers;

import com.fhws.zeiterfassung.boundaries.useCases.DeleteUsersWorkedTime;
import com.fhws.zeiterfassung.boundaries.useCases.GetUsersWorkedTime;
import com.fhws.zeiterfassung.boundaries.useCases.SaveUsersTime;
import com.fhws.zeiterfassung.exceptions.InvalidDataException;
import com.fhws.zeiterfassung.exceptions.UserDoesNotExistException;
import com.fhws.zeiterfassung.viewModels.WorkedTimeViewModel;
import com.fhws.zeiterfassung.utils.LoggedInUserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;

@RestController
public class WorkedTimeController {

    private final LoggedInUserUtil loggedInUserUtil;
    private final GetUsersWorkedTime getUsersWorkedTime;
    private final SaveUsersTime saveUsersTime;
    private final DeleteUsersWorkedTime deleteUsersTime;
    private final String userDoesNotExistMessage = "User does not exist";

    @Autowired
    public WorkedTimeController(LoggedInUserUtil loggedInUserUtil,
                                GetUsersWorkedTime getUsersWorkedTime,
                                SaveUsersTime saveUsersTime,
                                DeleteUsersWorkedTime deleteUsersTime) {
        this.loggedInUserUtil = loggedInUserUtil;
        this.getUsersWorkedTime = getUsersWorkedTime;
        this.saveUsersTime = saveUsersTime;
        this.deleteUsersTime = deleteUsersTime;
    }

    @RequestMapping(value = "/times", method = RequestMethod.POST)
    public ResponseEntity<?> getTimes(@RequestHeader String authorization) {
        try {
            return new ResponseEntity<>(
                    getUsersWorkedTime.get(loggedInUserUtil.getUsernameFromAuthorizationToken(authorization)),
                    HttpStatus.OK);
        } catch (UserDoesNotExistException e) {
            return new ResponseEntity<>(userDoesNotExistMessage, HttpStatus.FORBIDDEN);
        }
    }

    @RequestMapping(value = "/add-times", method = RequestMethod.POST)
    public ResponseEntity<?> addTimes(@RequestBody ArrayList<WorkedTimeViewModel> workedTimeViewModels, @RequestHeader String authorization) {
        try {
            saveUsersTime.save(workedTimeViewModels, loggedInUserUtil.getUsernameFromAuthorizationToken(authorization));
        } catch (UserDoesNotExistException e) {
            return new ResponseEntity<>(userDoesNotExistMessage, HttpStatus.FORBIDDEN);
        } catch (InvalidDataException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(value = "/delete-times", method = RequestMethod.POST)
    public ResponseEntity<?> deleteTimes(@RequestBody ArrayList<Long> ids, @RequestHeader String authorization) {
        try {
            deleteUsersTime.delete(ids, loggedInUserUtil.getUsernameFromAuthorizationToken(authorization));
        } catch (UserDoesNotExistException e) {
            return new ResponseEntity<>(userDoesNotExistMessage, HttpStatus.FORBIDDEN);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(value = "/test-date", method = RequestMethod.POST)
    public ResponseEntity<?> testDate(@RequestBody Timestamp timestamp) {
        LocalDateTime dateTime = timestamp.toLocalDateTime();
        Timestamp newTimestamp = Timestamp.valueOf(dateTime);
        return new ResponseEntity<>(newTimestamp.getTime(), HttpStatus.OK);
    }
}
