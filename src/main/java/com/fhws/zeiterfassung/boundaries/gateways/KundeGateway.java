package com.fhws.zeiterfassung.boundaries.gateways;

import com.fhws.zeiterfassung.entities.Kunde;
import com.fhws.zeiterfassung.entities.User;
import com.fhws.zeiterfassung.exceptions.EntityNotFoundException;
import com.fhws.zeiterfassung.exceptions.InvalidDataException;

import java.util.ArrayList;

public interface KundeGateway {

    ArrayList<Kunde> getAll();

    ArrayList<Kunde> getAllByUser(User user);

    void addKunden(ArrayList<Kunde> kunden);

    Kunde getById(Long id) throws EntityNotFoundException;

    void removeKunden(ArrayList<Kunde> kunden) throws InvalidDataException;
}
