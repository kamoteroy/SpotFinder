package com.spotfinder.Models;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class ParkingSlot {

    @Id
    private String slotName;
    private boolean occupy;
    private String user;
    private String timeIn;
    private String date;

    public ParkingSlot() {}
    
    public ParkingSlot(String slotName, boolean occupy) {
        this.slotName = slotName;
        this.occupy = occupy;
    }

    public ParkingSlot(String slotName, boolean occupy, String user) {
		super();
		this.slotName = slotName;
		this.occupy = occupy;
		this.user = user;
	}

	public ParkingSlot(String slotName, boolean occupy, String user, String timeIn, String date) {
		super();
		this.slotName = slotName;
		this.occupy = occupy;
		this.user = user;
		this.timeIn = timeIn;
		this.date = date;
	}

	public String getSlotName() { return slotName; }
    public void setSlotName(String slotName) { this.slotName = slotName; }
    public boolean isOccupy() { return occupy; }
    public void setOccupy(boolean occupy) { this.occupy = occupy; }
	public String getUser() { return user; }
	public void setUser(String user) { this.user = user; }
	public String getTimeIn() { return timeIn; }
	public void setTimeIn(String timeIn) { this.timeIn = timeIn; }
	public String getDate() { return date; }
	public void setDate(String date) { this.date = date; }
}
