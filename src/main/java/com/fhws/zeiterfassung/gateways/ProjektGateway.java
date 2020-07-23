package com.fhws.zeiterfassung.gateways;

import com.fhws.zeiterfassung.entities.Projekt;
import com.fhws.zeiterfassung.entities.User;
import com.fhws.zeiterfassung.exceptions.InvalidDataException;

import java.util.ArrayList;

public interface ProjektGateway {

    void addProjekte(ArrayList<Projekt> projekte);

    ArrayList<Projekt> getAllByUser(User user);

    void removeProjekte(ArrayList<Projekt> projekte) throws InvalidDataException;
}
