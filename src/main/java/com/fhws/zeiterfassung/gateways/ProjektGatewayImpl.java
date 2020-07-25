package com.fhws.zeiterfassung.gateways;

import com.fhws.zeiterfassung.boundaries.gateways.ProjektGateway;
import com.fhws.zeiterfassung.entities.Projekt;
import com.fhws.zeiterfassung.entities.User;
import com.fhws.zeiterfassung.exceptions.InvalidDataException;
import com.fhws.zeiterfassung.repositories.ProjektRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
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

    @Override
    public void removeProjekte(ArrayList<Projekt> projekte) throws InvalidDataException {
        try {
            repository.deleteAll(projekte);
        } catch (Exception e) {
            throw new InvalidDataException();
        }
    }
}
