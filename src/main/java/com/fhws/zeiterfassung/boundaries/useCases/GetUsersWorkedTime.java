package com.fhws.zeiterfassung.boundaries.useCases;

import com.fhws.zeiterfassung.exceptions.UserDoesNotExistException;
import com.fhws.zeiterfassung.viewModels.WorkedTimeViewModel;

import java.util.ArrayList;

public interface GetUsersWorkedTime {

    ArrayList<WorkedTimeViewModel> get(String username) throws UserDoesNotExistException;
}
