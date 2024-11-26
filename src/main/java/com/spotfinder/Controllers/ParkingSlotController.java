package com.spotfinder.Controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import com.spotfinder.Models.ParkingSlot;
import com.spotfinder.Models.ParkingSlotRepository;
import com.spotfinder.Models.ParkingSlotService;

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

    @GetMapping("/updateSlotStatus/{slotName}")
    @ResponseBody
    public ResponseEntity<Long> updateSlotStatus(@PathVariable String slotName) {
        // Toggle the slot status
        parkingSlotService.updateSlotStatus(slotName);

        // Get the updated count of available parking slots
        List<ParkingSlot> parkingSlots = parkingSlotRepository.findAll();
        long occupiedCount = parkingSlots.stream()
            .filter(ParkingSlot::isOccupy)
            .count();
        long availableCount = parkingSlots.size() - occupiedCount;

        // Return the updated available count as a response
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
