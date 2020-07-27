package com.fhws.zeiterfassung.boundaries.useCases;

import com.fhws.zeiterfassung.exceptions.UserDoesNotExistException;
import com.fhws.zeiterfassung.viewModels.WorkedTimeViewModel;

import java.util.ArrayList;

public interface DeleteUsersWorkedTime {

    public void delete(ArrayList<Long> ids, String username) throws UserDoesNotExistException;
}
