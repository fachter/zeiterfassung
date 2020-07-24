package com.fhws.zeiterfassung.repositories;

import com.fhws.zeiterfassung.entities.User;
import com.fhws.zeiterfassung.entities.WorkedTime;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.ArrayList;

public interface WorkedTimeRepository extends JpaRepository<WorkedTime, Long> {

    ArrayList<WorkedTime> findAllByCreatedByOrderByStartTimeDesc(User user);
}
