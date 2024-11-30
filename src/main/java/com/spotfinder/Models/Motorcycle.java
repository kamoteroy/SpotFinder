package com.spotfinder.Models;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;

@Entity
public class Motorcycle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String user;
    
	public Long getId() { return id; }
	public void setId(Long id) { this.id = id; }
	public String getUser() { return user; }
	public void setUser(String user) { this.user = user; }
}