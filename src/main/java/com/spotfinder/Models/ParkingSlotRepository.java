package com.spotfinder.Models;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ParkingSlotRepository extends JpaRepository<ParkingSlot, String> {
    Optional<ParkingSlot> findBySlotName(String slotName);
}
