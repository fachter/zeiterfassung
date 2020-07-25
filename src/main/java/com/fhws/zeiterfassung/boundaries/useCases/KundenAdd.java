package com.fhws.zeiterfassung.boundaries.useCases;

import com.fhws.zeiterfassung.exceptions.InvalidDataException;
import com.fhws.zeiterfassung.exceptions.UserDoesNotExistException;
import com.fhws.zeiterfassung.viewModels.KundenViewModel;

import java.util.ArrayList;

public interface KundenAdd {

    void add(ArrayList<KundenViewModel> kundenViewModels, String username) throws InvalidDataException, UserDoesNotExistException;
}
