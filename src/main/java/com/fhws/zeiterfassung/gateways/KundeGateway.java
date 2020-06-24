package com.fhws.zeiterfassung.gateways;

import com.fhws.zeiterfassung.entities.Kunde;
import com.fhws.zeiterfassung.entities.User;
import com.fhws.zeiterfassung.exceptions.EntityNotFoundException;

import java.util.ArrayList;

public interface KundeGateway {

    ArrayList<Kunde> getAll();

    ArrayList<Kunde> getAllByUser(User user);

    void addKunden(ArrayList<Kunde> kunden);

    Kunde getById(Long id) throws EntityNotFoundException;
}