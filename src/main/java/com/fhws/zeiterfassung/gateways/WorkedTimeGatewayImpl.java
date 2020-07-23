package com.fhws.zeiterfassung.gateways;

import com.fhws.zeiterfassung.entities.User;
import com.fhws.zeiterfassung.entities.WorkedTime;
import com.fhws.zeiterfassung.repositories.WorkedTimeRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class WorkedTimeGatewayImpl implements WorkedTimeGateway {

    private final WorkedTimeRepository repository;

    public WorkedTimeGatewayImpl(WorkedTimeRepository repository) {
        this.repository = repository;
    }

    @Override
    public ArrayList<WorkedTime> getAllByUser(User user) {
        return repository.findAllByCreatedBy(user);
    }

    @Override
    public void saveAll(ArrayList<WorkedTime> workedTimes) {
        repository.saveAll(workedTimes);
    }

    public void flush() {
        repository.flush();
    }
}
