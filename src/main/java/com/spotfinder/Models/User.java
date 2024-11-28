package com.spotfinder.Models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "students")
public class User {

    @Id
    @Column(unique=true)
    String username;
    @Column(unique=true)
    String email;
    String password;
    String name;
    String address;
    String contact;
    String econtact;
    String plate;
    String img;
    String role;
    
    public User() {}
    
	public User(String username, String email, String password) {
		super();
		this.username = username;
		this.email = email;
		this.password = password;
	}

	public User(String username, String email, String password, String name, String address, String contact,
			String econtact, String plate, String img, String role) {
		super();
		this.username = username;
		this.email = email;
		this.password = password;
		this.name = name;
		this.address = address;
		this.contact = contact;
		this.econtact = econtact;
		this.plate = plate;
		this.img = img;
		this.role = role;
	}

	public String getRole() { return role; }
	public void setRole(String role) { this.role = role; }
	public String getUsername() { return username; }
	public void setUsername(String username) { this.username = username; }
	public String getEmail() { return email; }
	public void setEmail(String email) { this.email = email; }
	public String getPassword() { return password; }
	public void setPassword(String password) { this.password = password; }
	public String getName() { return name;}
	public void setName(String name) { this.name = name; }
	public String getAddress() { return address; }
	public void setAddress(String address) { this.address = address; }
	public String getContact() { return contact; }
	public void setContact(String contact) { this.contact = contact; }
	public String getEcontact() { return econtact; }
	public void setEcontact(String econtact) { this.econtact = econtact; }
	public String getImg() { return img; }
	public void setImg(String img) { this.img = img; }
	public String getPlate() { return plate; }
	public void setPlate(String plate) { this.plate = plate; }
}