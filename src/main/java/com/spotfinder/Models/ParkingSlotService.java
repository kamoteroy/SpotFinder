package com.spotfinder.Models;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ParkingSlotService {

    @Autowired
    private ParkingSlotRepository parkingSlotRepository;

    public boolean isOccupied(String slotName) {
        return parkingSlotRepository.findBySlotName(slotName)
                .map(ParkingSlot::isOccupy)
                .orElse(false);
    }
   
    public void updateSlotStatus(String slotName) {
        ParkingSlot slot = parkingSlotRepository.findBySlotName(slotName)
                .orElse(new ParkingSlot(slotName, false)); // if not found, create new one

        slot.setOccupy(!slot.isOccupy()); // toggle the occupy status
        parkingSlotRepository.save(slot); // save the updated slot
    }
}
