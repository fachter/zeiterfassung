package com.fhws.zeiterfassung.repositories;

import com.fhws.zeiterfassung.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByUsername(String username);
}
