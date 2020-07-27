package com.fhws.zeiterfassung.useCases;

import com.fhws.zeiterfassung.boundaries.gateways.UserGateway;
import com.fhws.zeiterfassung.boundaries.gateways.WorkedTimeGateway;
import com.fhws.zeiterfassung.boundaries.useCases.DeleteUsersWorkedTime;
import com.fhws.zeiterfassung.entities.User;
import com.fhws.zeiterfassung.entities.WorkedTime;
import com.fhws.zeiterfassung.exceptions.UserDoesNotExistException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class DeleteUsersWorkedTimeUseCase implements DeleteUsersWorkedTime {

    private final WorkedTimeGateway workedTimeGateway;
    private final UserGateway userGateway;
    private ArrayList<WorkedTime> timesToRemove;
    private ArrayList<WorkedTime> existingTimes;

    public DeleteUsersWorkedTimeUseCase(WorkedTimeGateway workedTimeGateway, UserGateway userGateway) {
        this.workedTimeGateway = workedTimeGateway;
        this.userGateway = userGateway;
    }

    @Override
    public void delete(ArrayList<Long> ids, String username) throws UserDoesNotExistException {
        User user = userGateway.getUserByUsername(username);
        existingTimes = workedTimeGateway.getAllByUserOrderedByDate(user);
        timesToRemove = new ArrayList<>();
        addWorkedTimesToRemove(ids);
        if (timesToRemove.size() > 0)
            workedTimeGateway.remove(timesToRemove);
    }

    private void addWorkedTimesToRemove(ArrayList<Long> ids) {
        for (Long id : ids) {
            for (WorkedTime existingTime : existingTimes) {
                if (existingTime.getId().equals(id))
                    timesToRemove.add(existingTime);
            }
        }
    }
}
