package com.spotfinder.Models;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;

@Entity
public class History {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-increment primary key
    private Long id;  // Changed to Long for auto-increment
    private String user;
    private String date;
    private String slot;
    private String timeIn;
    private String timeOut;
    private String duration;
    
    public History() {}

    public History(Long id, String user, String date, String slot, String timeIn, String timeOut, String duration) {
        super();
        this.id = id;
        this.user = user;
        this.date = date;
        this.slot = slot;
        this.timeIn = timeIn;
        this.timeOut = timeOut;
        this.duration = duration;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getUser() { return user; }
    public void setUser(String user) { this.user = user; }
    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }
    public String getSlot() { return slot; }
    public void setSlot(String slot) { this.slot = slot; }
    public String getTimeIn() { return timeIn; }
    public void setTimeIn(String timeIn) { this.timeIn = timeIn; }
    public String getTimeOut() { return timeOut; }
    public void setTimeOut(String timeOut) { this.timeOut = timeOut; }
    public String getDuration() { return duration; }
    public void setDuration(String duration) { this.duration = duration; }
}
