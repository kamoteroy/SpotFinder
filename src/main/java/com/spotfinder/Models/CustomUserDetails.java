package com.spotfinder.Models;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class CustomUserDetails implements UserDetails {
	
	private String username;
	private String email;
	private String password;
	private String name;
	private String address;
	private String contact;
	private String econtact;
	private String plate;
	private String img;
	private String role;
	private Collection<? extends GrantedAuthority> authorities;

	
	public CustomUserDetails(String username, String email, String password,
			Collection<? extends GrantedAuthority> authorities) {
		this.username = username;
		this.email = email;
		this.password = password;
		this.authorities = authorities;
	}
	public CustomUserDetails(String username, String email, String password, String name, String address,
			String contact, String econtact, String plate, String img, String role,
			Collection<? extends GrantedAuthority> authorities) {
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
		this.authorities = authorities;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		// TODO Auto-generated method stub
		return authorities;
	}

	@Override
	public String getPassword() { return password; }

	public String getName() { return name; }

	public String getAddress() { return address; }

	public String getContact() { return contact; }

	public String getEcontact() { return econtact; }

	public String getImg() { return img; }
	
	public String getEmail() { return email; }
	
	public String getPlate() { return plate; }

	public void setAddress(String address) { this.address = address; }

	public void setContact(String contact) { this.contact = contact; }

	public void setEcontact(String econtact) { this.econtact = econtact; }

	public void setImg(String img) { this.img = img; }
	
	public void setPlate(String plate) { this.plate = plate; }

	public void setPassword(String password) { this.password = password; }
	
	

	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	@Override
	public String getUsername() { return username; }

	@Override
	public boolean isAccountNonExpired() { return true; }
	
	@Override
	public boolean isAccountNonLocked() { return true; }

	@Override
	public boolean isCredentialsNonExpired() { return true; }
}
