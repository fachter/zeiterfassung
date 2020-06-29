package com.fhws.zeiterfassung.boundaries;

import com.fhws.zeiterfassung.exceptions.InvalidDataException;
import com.fhws.zeiterfassung.exceptions.UserDoesNotExistException;
import com.fhws.zeiterfassung.models.WorkedTimeViewModel;

import java.util.ArrayList;

public interface SaveUsersTime {

    void save(ArrayList<WorkedTimeViewModel> workedTimeViewModels, String username) throws UserDoesNotExistException, InvalidDataException;
}
