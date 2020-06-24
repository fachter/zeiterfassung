package com.fhws.zeiterfassung.controllers;

import com.fhws.zeiterfassung.boundaries.KundenAdd;
import com.fhws.zeiterfassung.boundaries.KundenGet;
import com.fhws.zeiterfassung.exceptions.InvalidDataException;
import com.fhws.zeiterfassung.exceptions.UserDoesNotExistException;
import com.fhws.zeiterfassung.models.KundenViewModel;
import com.fhws.zeiterfassung.utils.JwtUtil;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
public class ConfigController {

    private final KundenAdd kundenAdd;
    private final KundenGet kundenGet;
    private final JwtUtil jwtUtil;

    @Autowired
    public ConfigController(KundenAdd kundenAdd, KundenGet kundenGet, JwtUtil jwtUtil) {
        this.kundenAdd = kundenAdd;
        this.kundenGet = kundenGet;
        this.jwtUtil = jwtUtil;
    }

    @RequestMapping(value = "/add-kunde", method = RequestMethod.POST)
    public ResponseEntity<?> addKunde(@RequestBody ArrayList<KundenViewModel> kundenViewModels,
                                      @RequestHeader String authorization) {
        String usernameFromToken = getUsernameFromToken(authorization);
        try {
            kundenAdd.add(kundenViewModels, usernameFromToken);
        } catch (InvalidDataException e) {
            return new ResponseEntity<>("Invalid Data", HttpStatus.BAD_REQUEST);
        } catch (UserDoesNotExistException e) {
            return new ResponseEntity<>("User does not exist!", HttpStatus.FORBIDDEN);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    private String getUsernameFromToken(String authorization) {
        String token = authorization.substring(7);
        return jwtUtil.extractUsername(token);
    }

    @RequestMapping(value= "/kunden", method = RequestMethod.POST)
    public ResponseEntity<?> getKunden(@RequestHeader String authorization) {
        String usernameFromToken = getUsernameFromToken(authorization);
        ArrayList<KundenViewModel> kunden = null;
        try {
            kunden = kundenGet.get(usernameFromToken);
        } catch (UserDoesNotExistException e) {
            return new ResponseEntity<>("User does not exist", HttpStatus.FORBIDDEN);
        }
        return new ResponseEntity<>(kunden, HttpStatus.OK);
    }
}
