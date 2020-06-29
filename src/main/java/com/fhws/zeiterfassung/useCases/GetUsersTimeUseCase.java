package com.fhws.zeiterfassung.useCases;

import com.fhws.zeiterfassung.boundaries.GetUsersTime;
import com.fhws.zeiterfassung.models.TimeViewModel;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class GetUsersTimeUseCase implements GetUsersTime {
    @Override
    public ArrayList<TimeViewModel> get(String username) {
        return new ArrayList<>();
    }
}
