package com.fhws.zeiterfassung.useCases;

import com.fhws.zeiterfassung.boundaries.SaveUsersTime;
import com.fhws.zeiterfassung.models.WorkedTimeViewModel;
import org.springframework.stereotype.Service;

@Service
public class SaveUsersTimeUseCase implements SaveUsersTime {

    @Override
    public void save(WorkedTimeViewModel workedTimeViewModel, String username) {

    }
}
