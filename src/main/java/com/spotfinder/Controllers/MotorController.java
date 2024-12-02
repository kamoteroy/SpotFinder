package com.spotfinder.Controllers;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.spotfinder.Models.MotorRepository;
import com.spotfinder.Models.Motorcycle;

@RestController
@RequestMapping("/addOrRemoveMotorcycle")
public class MotorController {

    @Autowired
    private MotorRepository motorcycleRepository;

    @PostMapping
    public ResponseEntity<Map<String, Object>> addOrRemoveMotorcycle(@RequestBody Map<String, String> request) {
        String userId = request.get("userId");

        // Create a response map
        Map<String, Object> response = new HashMap<>();

        // Check if the motorcycle already exists in the database
        Optional<Motorcycle> existingMotorcycle = motorcycleRepository.findByUser(userId);

        if (existingMotorcycle.isPresent()) {
            // Remove the motorcycle if it already exists
            motorcycleRepository.delete(existingMotorcycle.get());
            response.put("success", true);
            response.put("message", "Motorcycle removed successfully.");
            response.put("updatedMotorCount", getMotorcycleCount()); // Return updated count
        } else {
            // Add the motorcycle if it does not exist
            Motorcycle newMotorcycle = new Motorcycle();
            newMotorcycle.setUser(userId);
            motorcycleRepository.save(newMotorcycle);
            response.put("success", true);
            response.put("message", "Motorcycle added successfully.");
            response.put("updatedMotorCount", getMotorcycleCount()); // Return updated count
        }

        return ResponseEntity.ok(response);
    }

    // Method to get the updated count of motorcycles
    private int getMotorcycleCount() {
        return (int) motorcycleRepository.count(); // Assuming your motorcycle table tracks count
    }
}
