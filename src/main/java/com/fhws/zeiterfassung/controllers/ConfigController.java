package com.fhws.zeiterfassung.controllers;

import com.fhws.zeiterfassung.boundaries.KundenAdd;
import com.fhws.zeiterfassung.models.KundenViewModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ConfigController {

    private final KundenAdd kundenAdd;

    @Autowired
    public ConfigController(KundenAdd kundenAdd) {
        this.kundenAdd = kundenAdd;
    }

    @RequestMapping(value = "/add-kunde", method = RequestMethod.POST)
    public ResponseEntity<?> addKunde(@RequestBody KundenViewModel[] kundenViewModels) {
        kundenAdd.add(kundenViewModels);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
