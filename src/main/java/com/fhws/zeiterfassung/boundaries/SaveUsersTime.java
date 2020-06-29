package com.fhws.zeiterfassung.boundaries;

import com.fhws.zeiterfassung.models.WorkedTimeViewModel;

public interface SaveUsersTime {

    void save(WorkedTimeViewModel workedTimeViewModel, String username);
}
