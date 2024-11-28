package com.spotfinder.Controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import com.spotfinder.Models.ParkingSlot;
import com.spotfinder.Models.ParkingSlotRepository;
import com.spotfinder.Models.ParkingSlotService;
import com.spotfinder.Models.SlotStatusRequest;

@Controller
public class ParkingSlotController {

    @Autowired
    private ParkingSlotService parkingSlotService;
    @Autowired
    private ParkingSlotRepository parkingSlotRepository;
    
    private void slotoccupied(Model model) {
        List<ParkingSlot> parkingSlots = parkingSlotRepository.findAll();
        // Filter the list where 'occupy' is true and count how many slots are occupied
        long occupiedCount = parkingSlots.stream()
            .filter(ParkingSlot::isOccupy)  // Filters parking slots where 'occupy' is true
            .count();  // Counts the number of elements after filtering
        model.addAttribute("occupiedCount", occupiedCount);
        model.addAttribute("parkingSlots", parkingSlots);
    }

    @PostMapping("/updateSlotStatus/{slotName}")
    @ResponseBody
    public ResponseEntity<Long> updateSlotStatus(@PathVariable String slotName, @RequestBody SlotStatusRequest request) {
        String username = request.getUsername();
        String slot = request.getSlot();

        // Check if the slot exists
        Optional<ParkingSlot> parkingSlotOptional = parkingSlotRepository.findById(slotName);
        if (!parkingSlotOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(-1L); // Slot not found
        }

        ParkingSlot parkingSlot = parkingSlotOptional.get();

        // Check if the user is already occupying a different slot (not the selected slot)
        Optional<ParkingSlot> existingSlot = parkingSlotRepository.findByUser(username);
        if (existingSlot.isPresent() && !existingSlot.get().getSlotName().equals(slotName)) {
            // If the user is already occupying a different slot, return a 409 Conflict status with a message
            return ResponseEntity.status(HttpStatus.CONFLICT).body(-3L); // -3L indicates user already parked elsewhere
        }

        // Check if the slot is occupied
        if (parkingSlot.isOccupy()) {
            // If the slot is occupied, check if the username matches the user occupying the slot
            if (!username.equals(parkingSlot.getUser())) {
                // If the username doesn't match, return a 403 Forbidden error
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                                     .body(-2L); // -2L indicates unauthorized access
            }
        }

        // Toggle the occupancy status
        if (!parkingSlot.isOccupy()) {
            parkingSlot.setOccupy(true);
            parkingSlot.setUser(username); // Assign the username if slot is being occupied
        } else {
            parkingSlot.setOccupy(false);
            parkingSlot.setUser(null); // Clear the user if slot is being unoccupied
        }

        // Save the updated parking slot
        parkingSlotRepository.save(parkingSlot);

        // Calculate available count
        List<ParkingSlot> parkingSlots = parkingSlotRepository.findAll();
        long occupiedCount = parkingSlots.stream()
                .filter(ParkingSlot::isOccupy)
                .count();
        long availableCount = parkingSlots.size() - occupiedCount;

        // Return the updated available count
        return ResponseEntity.ok(availableCount);
    }



    @GetMapping("/map/backgate")
    public String getBackgateSlots(Model model) {
    	slotoccupied(model);
        model.addAttribute("parkingSlotService", parkingSlotService);
        return "backgate"; // return the HTML view
    }
    @GetMapping("/map/GLE")
    public String getGLESlots(Model model) {
    	slotoccupied(model);
        model.addAttribute("parkingSlotService", parkingSlotService);
        return "GLE"; // return the HTML view
    }
    @GetMapping("/map/open-area")
    public String getOAreaSlots(Model model) {
    	slotoccupied(model);
        model.addAttribute("parkingSlotService", parkingSlotService);
        return "oarea"; // return the HTML view
    }
    @GetMapping("/map/phys")
    public String getPhysSlots(Model model) {
    	slotoccupied(model);
        model.addAttribute("parkingSlotService", parkingSlotService);
        return "phys"; // return the HTML view
    }
}
