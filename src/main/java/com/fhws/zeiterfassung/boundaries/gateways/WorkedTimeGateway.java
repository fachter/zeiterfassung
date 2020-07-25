package com.fhws.zeiterfassung.boundaries.gateways;

import com.fhws.zeiterfassung.entities.User;
import com.fhws.zeiterfassung.entities.WorkedTime;

import java.util.ArrayList;

public interface WorkedTimeGateway {

    ArrayList<WorkedTime> getAllByUserOrderedByDate(User user);

    void saveAll(ArrayList<WorkedTime> workedTimes);

    void flush();
}
