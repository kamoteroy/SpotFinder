package com.spotfinder.Models;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface MotorRepository extends JpaRepository<Motorcycle, Long> {
	Optional<Motorcycle> findByUser(String user);
    void deleteByUser(String user);
}
