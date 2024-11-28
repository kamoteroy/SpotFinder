package com.spotfinder.Controllers;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

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

import com.spotfinder.Models.History;
import com.spotfinder.Models.HistoryRepository;
import com.spotfinder.Models.ParkingSlot;
import com.spotfinder.Models.ParkingSlotRepository;
import com.spotfinder.Models.ParkingSlotService;
import com.spotfinder.Models.SlotStatusRequest;
import jakarta.servlet.http.HttpSession;

@Controller
public class ParkingSlotController {

    @Autowired
    private ParkingSlotService parkingSlotService;
    @Autowired
    private ParkingSlotRepository parkingSlotRepository;
    @Autowired
    private HistoryRepository historyRepository;
   
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
        String timeIn = request.getTimeIn();  // Get timeIn from the request
        String date = request.getDate();      // Get date from the request

        // Check if the slot exists
        Optional<ParkingSlot> parkingSlotOptional = parkingSlotRepository.findById(slotName);
        if (!parkingSlotOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(-1L); // Slot not found
        }

        ParkingSlot parkingSlot = parkingSlotOptional.get();

        // Check if the user is already occupying a different slot (not the selected slot)
        Optional<ParkingSlot> existingSlot = parkingSlotRepository.findByUser(username);
        if (existingSlot.isPresent() && !existingSlot.get().getSlotName().equals(slotName)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(-3L); // -3L indicates user already parked elsewhere
        }

        // Check if the slot is occupied
        if (parkingSlot.isOccupy()) {
            if (!username.equals(parkingSlot.getUser())) {
                // If the username doesn't match, return a 403 Forbidden error
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(-2L); // -2L indicates unauthorized access
            }

            // Slot is occupied, unpark the user and record history
            // Calculate the timeOut and duration
            String timeOut = new SimpleDateFormat("HH:mm:ss").format(new Date()); // Current time
            long durationInMillis = calculateDurationInMillis(parkingSlot.getTimeIn(), timeOut); // Duration calculation
            String duration = formatDuration(durationInMillis); // Format the duration (e.g., "2 hours 30 minutes")
            System.out.println(UUID.randomUUID().toString());
            // Create a new history record
            History history = new History();
            history.setDate(parkingSlot.getDate());
            history.setSlot(parkingSlot.getSlotName());
            history.setTimeIn(parkingSlot.getTimeIn());
            history.setTimeOut(timeOut);
            history.setUser(username);
            history.setDuration(duration);

            // Save the history record to the database
            historyRepository.save(history);

            // Mark the slot as unoccupied
            parkingSlot.setOccupy(false);
            parkingSlot.setUser(null);     // Clear the user if slot is being unoccupied
            parkingSlot.setTimeIn(null);   // Clear timeIn when the user unparks
            parkingSlot.setDate(null);     // Clear date when the user unparks

            // Save the updated parking slot
            parkingSlotRepository.save(parkingSlot);
        } else {
            // Slot is not occupied, park the user
            parkingSlot.setOccupy(true);
            parkingSlot.setUser(username); // Assign the username if slot is being occupied
            parkingSlot.setTimeIn(timeIn); // Set timeIn when the user occupies the slot
            parkingSlot.setDate(date);     // Set date when the user occupies the slot

            // Save the updated parking slot
            parkingSlotRepository.save(parkingSlot);
        }

        // Calculate available count
        List<ParkingSlot> parkingSlots = parkingSlotRepository.findAll();
        long occupiedCount = parkingSlots.stream().filter(ParkingSlot::isOccupy).count();
        long availableCount = parkingSlots.size() - occupiedCount;

        // Return the updated available count
        return ResponseEntity.ok(availableCount);
    }

    // Helper method to calculate duration in milliseconds
    private long calculateDurationInMillis(String timeIn, String timeOut) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
            Date dateIn = sdf.parse(timeIn);
            Date dateOut = sdf.parse(timeOut);

            return dateOut.getTime() - dateIn.getTime(); // Duration in milliseconds
        } catch (ParseException e) {
            e.printStackTrace();
            return 0;
        }
    }

    // Helper method to format duration in a readable format (e.g., "2 hours 30 minutes")
    private String formatDuration(long durationInMillis) {
        long hours = durationInMillis / (1000 * 60 * 60);
        long minutes = (durationInMillis % (1000 * 60 * 60)) / (1000 * 60);

        return String.format("%d hours %d minutes", hours, minutes);
    }

    @GetMapping("/map/backgate")
    public String getBackgateSlots(Model model, HttpSession session) {
    	model.addAttribute("user", session.getAttribute("user"));
    	slotoccupied(model);
        model.addAttribute("parkingSlotService", parkingSlotService);
        return "backgate"; // return the HTML view
    }
    @GetMapping("/map/GLE")
    public String getGLESlots(Model model, HttpSession session) {
    	model.addAttribute("user", session.getAttribute("user"));
    	slotoccupied(model);
        model.addAttribute("parkingSlotService", parkingSlotService);
        return "GLE"; // return the HTML view
    }
    @GetMapping("/map/open-area")
    public String getOAreaSlots(Model model, HttpSession session) {
    	model.addAttribute("user", session.getAttribute("user"));
    	slotoccupied(model);
        model.addAttribute("parkingSlotService", parkingSlotService);
        return "oarea"; // return the HTML view
    }
    @GetMapping("/map/phys")
    public String getPhysSlots(Model model, HttpSession session) {
    	model.addAttribute("user", session.getAttribute("user"));
    	slotoccupied(model);
        model.addAttribute("parkingSlotService", parkingSlotService);
        return "phys"; // return the HTML view
    }
}
