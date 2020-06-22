package com.fhws.zeiterfassung.gateways;

import com.fhws.zeiterfassung.entities.Kunde;
import com.fhws.zeiterfassung.exceptions.EntityNotFoundException;
import com.fhws.zeiterfassung.repositories.KundeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class KundeGatewayImpl implements KundeGateway {

    private final KundeRepository repository;

    @Autowired
    public KundeGatewayImpl(KundeRepository repository) {
        this.repository = repository;
    }

    @Override
    public ArrayList<Kunde> getAll() {
        return repository.findAll();
    }

    @Override
    public void addKunden(ArrayList<Kunde> kunden) {
        repository.saveAll(kunden);
    }

    @Override
    public Kunde getById(Long id) throws EntityNotFoundException {
        Kunde kunde = repository.findKundeById(id);
        if (kunde == null)
            throw new EntityNotFoundException();
        return kunde;
    }
}
