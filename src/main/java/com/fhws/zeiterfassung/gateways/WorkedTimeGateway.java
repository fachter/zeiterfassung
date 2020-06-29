package com.fhws.zeiterfassung.gateways;

import com.fhws.zeiterfassung.entities.User;
import com.fhws.zeiterfassung.entities.WorkedTime;

import java.util.ArrayList;

public interface WorkedTimeGateway {

    ArrayList<WorkedTime> getAllByUser(User user);

    void saveAll(ArrayList<WorkedTime> workedTimes);
}
