package com.fhws.zeiterfassung.boundaries;

import com.fhws.zeiterfassung.models.TimeViewModel;

import java.util.ArrayList;

public interface GetUsersTime {

    ArrayList<TimeViewModel> get(String username);
}
