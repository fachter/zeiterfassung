package com.fhws.zeiterfassung.controllers;

import com.fhws.zeiterfassung.boundaries.KundenAdd;
import com.fhws.zeiterfassung.boundaries.KundenGet;
import com.fhws.zeiterfassung.boundaries.ProjekteAdd;
import com.fhws.zeiterfassung.boundaries.ProjekteGet;
import com.fhws.zeiterfassung.exceptions.InvalidDataException;
import com.fhws.zeiterfassung.exceptions.UserDoesNotExistException;
import com.fhws.zeiterfassung.models.KundenViewModel;
import com.fhws.zeiterfassung.models.ProjektViewModel;
import com.fhws.zeiterfassung.utils.LoggedInUserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
public class ConfigController {

    private final KundenAdd kundenAdd;
    private final KundenGet kundenGet;
    private final ProjekteAdd projekteAdd;
    private final ProjekteGet projekteGet;
    private final LoggedInUserUtil loggedInUserUtil;

    @Autowired
    public ConfigController(KundenAdd kundenAdd,
                            KundenGet kundenGet,
                            ProjekteAdd projekteAdd,
                            ProjekteGet projekteGet,
                            LoggedInUserUtil loggedInUserUtil) {
        this.kundenAdd = kundenAdd;
        this.kundenGet = kundenGet;
        this.projekteAdd = projekteAdd;
        this.projekteGet = projekteGet;
        this.loggedInUserUtil = loggedInUserUtil;
    }

    @RequestMapping(value = "/add-kunden", method = RequestMethod.POST)
    public ResponseEntity<?> addKunden(@RequestBody ArrayList<KundenViewModel> kundenViewModels,
                                       @RequestHeader String authorization) {
        String usernameFromToken = loggedInUserUtil.getUsernameFromAuthorizationToken(authorization);
        try {
            kundenAdd.add(kundenViewModels, usernameFromToken);
        } catch (InvalidDataException e) {
            return new ResponseEntity<>("Invalid Data", HttpStatus.BAD_REQUEST);
        } catch (UserDoesNotExistException e) {
            return new ResponseEntity<>("User does not exist!", HttpStatus.FORBIDDEN);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(value = "/kunden", method = RequestMethod.POST)
    public ResponseEntity<?> getKunden(@RequestHeader String authorization) {
        String usernameFromToken = loggedInUserUtil.getUsernameFromAuthorizationToken(authorization);
        ArrayList<KundenViewModel> kunden;
        try {
            kunden = kundenGet.get(usernameFromToken);
        } catch (UserDoesNotExistException e) {
            return new ResponseEntity<>("User does not exist", HttpStatus.FORBIDDEN);
        }
        return new ResponseEntity<>(kunden, HttpStatus.OK);
    }

    @RequestMapping(value = "/add-projekte", method = RequestMethod.POST)
    public ResponseEntity<?> addProjekte(@RequestBody ArrayList<ProjektViewModel> projektViewModels,
                                         @RequestHeader String authorization) {
        try {
            projekteAdd.add(projektViewModels, loggedInUserUtil.getUsernameFromAuthorizationToken(authorization));
        } catch (UserDoesNotExistException e) {
            return new ResponseEntity<>("User does not exist", HttpStatus.FORBIDDEN);
        } catch (InvalidDataException e) {
            return new ResponseEntity<>("Invalid Data", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(value = "/projekte", method = RequestMethod.POST)
    public ResponseEntity<?> getProjekte(@RequestHeader String authorization) {
        try {
            return new ResponseEntity<>(projekteGet.get(loggedInUserUtil.getUsernameFromAuthorizationToken(authorization)), HttpStatus.OK);
        } catch (UserDoesNotExistException e) {
            return new ResponseEntity<>("User does not exist!", HttpStatus.FORBIDDEN);
        }
    }
}
