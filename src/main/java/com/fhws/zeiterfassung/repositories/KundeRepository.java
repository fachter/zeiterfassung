package com.fhws.zeiterfassung.repositories;

import com.fhws.zeiterfassung.entities.Kunde;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.ArrayList;

public interface KundeRepository extends JpaRepository<Kunde, Long> {

    ArrayList<Kunde> findAll();

    Kunde findKundeById(Long id);
}
