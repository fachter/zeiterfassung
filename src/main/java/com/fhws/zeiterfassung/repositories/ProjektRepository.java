package com.fhws.zeiterfassung.repositories;

import com.fhws.zeiterfassung.entities.Projekt;
import com.fhws.zeiterfassung.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.ArrayList;

public interface ProjektRepository extends JpaRepository<Projekt, Long> {

    ArrayList<Projekt> findAllByCreatedBy(User user);
}
