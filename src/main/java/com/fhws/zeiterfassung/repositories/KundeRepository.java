package com.fhws.zeiterfassung.repositories;

import com.fhws.zeiterfassung.entities.Kunde;
import com.fhws.zeiterfassung.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.ArrayList;

public interface KundeRepository extends JpaRepository<Kunde, Long> {

    ArrayList<Kunde> findAll();

    ArrayList<Kunde> findAllByCreatedBy(User user);

    Kunde findKundeById(Long id);
}
