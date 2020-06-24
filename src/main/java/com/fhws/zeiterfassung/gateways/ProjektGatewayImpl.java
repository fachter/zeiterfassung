package com.fhws.zeiterfassung.gateways;

import com.fhws.zeiterfassung.entities.Projekt;
import com.fhws.zeiterfassung.entities.User;
import com.fhws.zeiterfassung.repositories.ProjektRepository;

import java.util.ArrayList;

public class ProjektGatewayImpl implements ProjektGateway {

    private final ProjektRepository repository;

    public ProjektGatewayImpl(ProjektRepository repository) {
        this.repository = repository;
    }

    @Override
    public void addProjekte(ArrayList<Projekt> projekte) {
        repository.saveAll(projekte);
    }

    @Override
    public ArrayList<Projekt> getAllByUser(User user) {
        return repository.findAllByCreatedBy(user);
    }
}
