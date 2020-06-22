package com.fhws.zeiterfassung.boundaries;

import com.fhws.zeiterfassung.exceptions.EntityNotFoundException;
import com.fhws.zeiterfassung.exceptions.InvalidDataException;
import com.fhws.zeiterfassung.exceptions.UserDoesNotExistException;
import com.fhws.zeiterfassung.models.KundenViewModel;

import java.util.ArrayList;

public interface KundenAdd {

    void add(ArrayList<KundenViewModel> kundenViewModels, String username) throws InvalidDataException, UserDoesNotExistException;
}
