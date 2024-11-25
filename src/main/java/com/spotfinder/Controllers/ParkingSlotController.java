package com.spotfinder.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import com.spotfinder.Models.ParkingSlotService;

@Controller
public class ParkingSlotController {

    @Autowired
    private ParkingSlotService parkingSlotService;

    @GetMapping("/updateSlotStatus/{slotName}")
    @ResponseBody
    public ResponseEntity<Void> updateSlotStatus(@PathVariable String slotName) {
        parkingSlotService.updateSlotStatus(slotName);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/map/backgate")
    public String getBackgateSlots(Model model) {
        model.addAttribute("parkingSlotService", parkingSlotService);
        return "backgate"; // return the HTML view
    }
    @GetMapping("/map/GLE")
    public String getGLESlots(Model model) {
        model.addAttribute("parkingSlotService", parkingSlotService);
        return "GLE"; // return the HTML view
    }
    @GetMapping("/map/open-area")
    public String getOAreaSlots(Model model) {
        model.addAttribute("parkingSlotService", parkingSlotService);
        return "oarea"; // return the HTML view
    }
    @GetMapping("/map/phys")
    public String getPhysSlots(Model model) {
        model.addAttribute("parkingSlotService", parkingSlotService);
        return "phys"; // return the HTML view
    }
}
