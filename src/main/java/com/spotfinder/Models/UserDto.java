package com.spotfinder.Models;

public class UserDto {
	
	String username;
    String email;
    String password;
    String cpassword;
    
    public UserDto() {}
 
	public UserDto(String username, String email, String password, String cpassword) {
		this.username = username;
		this.email = email;
		this.password = password;
		this.cpassword = cpassword;
	}

	public String getUsername() { return username; }
	public void setUsername(String username) { this.username = username; }
	public String getEmail() { return email; }
	public void setEmail(String email) { this.email = email; }
	public String getPassword() { return password; }
	public void setPassword(String password) { this.password = password; }
	public String getCpassword() { return cpassword; }
	public void setCpassword(String cpassword) { this.cpassword = cpassword; }

}
