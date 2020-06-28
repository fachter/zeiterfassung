package com.fhws.zeiterfassung.useCases;

import com.fhws.zeiterfassung.boundaries.GetUsersTime;
import com.fhws.zeiterfassung.models.TimeViewModel;

import java.util.ArrayList;

public class GetUsersTimeUseCase implements GetUsersTime {
    @Override
    public ArrayList<TimeViewModel> get(String username) {
        return new ArrayList<>();
    }
}
