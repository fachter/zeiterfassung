package com.fhws.zeiterfassung.boundaries;

import com.fhws.zeiterfassung.exceptions.UserDoesNotExistException;
import com.fhws.zeiterfassung.models.KundenViewModel;

import java.util.ArrayList;

public interface KundenGet {

    ArrayList<KundenViewModel> get(String username) throws UserDoesNotExistException;
}
