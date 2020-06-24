package com.fhws.zeiterfassung.gateways;

import com.fhws.zeiterfassung.entities.Projekt;
import com.fhws.zeiterfassung.entities.User;

import java.util.ArrayList;

public interface ProjektGateway {

    void addProjekte(ArrayList<Projekt> projekte);

    ArrayList<Projekt> getAllByUser(User user);
}
