package com.spotfinder.Models;

public interface UserService {
	
	User findByUsername(String username);
	User save(UserDto userDto);
	void updateUser(User user);
	boolean existsByUsername(String username);
	boolean existsByEmail(String email);
	boolean isPasswordValid(String username, String password);
}