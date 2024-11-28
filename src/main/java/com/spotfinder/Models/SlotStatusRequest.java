package com.spotfinder.Models;

public class SlotStatusRequest {
    private String username;
    private String slot;

    // Default constructor (needed for deserialization)
    public SlotStatusRequest() {}

    // Getters and Setters
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getSlot() {
        return slot;
    }

    public void setSlot(String slot) {
        this.slot = slot;
    }
}
