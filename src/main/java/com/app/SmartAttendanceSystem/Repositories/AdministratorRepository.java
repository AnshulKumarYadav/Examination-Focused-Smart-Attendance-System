package com.app.SmartAttendanceSystem.Repositories;

import com.app.SmartAttendanceSystem.Models.Administrator;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AdministratorRepository extends JpaRepository<Administrator, Long> {
    Optional<Administrator> findByUsername(String username);
}
