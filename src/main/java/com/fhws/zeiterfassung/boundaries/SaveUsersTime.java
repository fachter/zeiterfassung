package com.fhws.zeiterfassung.boundaries;

import com.fhws.zeiterfassung.models.TimeViewModel;

public interface SaveUsersTime {

    void save(TimeViewModel timeViewModel, String username);
}
