package com.spotfinder.Models;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService{
	
	private UserRepository userRepository;
	
	@Autowired
	PasswordEncoder passwordEncoder;
	
	public UserServiceImpl(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@Override
	public User findByUsername(String username) {
		return userRepository.findByUsername(username);
	}

	@Override
	public User save(UserDto userDto) {
		User user = new User(userDto.getUsername(),userDto.getEmail(), passwordEncoder.encode(userDto.getPassword()));
		return userRepository.save(user);
	}
	
	public boolean existsByUsername(String username) {
	    return userRepository.findByUsername(username) != null;
	}

	public boolean existsByEmail(String email) {
	    return userRepository.findByEmail(email) != null;
	}
	
	public boolean isPasswordValid(String username, String password) {
	    User user = userRepository.findByUsername(username);
	    return user != null && passwordEncoder.matches(password, user.getPassword());
	}

}
