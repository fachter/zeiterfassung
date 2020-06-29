package com.fhws.zeiterfassung.boundaries;

import com.fhws.zeiterfassung.exceptions.UserDoesNotExistException;
import com.fhws.zeiterfassung.models.WorkedTimeViewModel;

import java.util.ArrayList;

public interface GetUsersWorkedTime {

    ArrayList<WorkedTimeViewModel> get(String username) throws UserDoesNotExistException;
}
