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
	
	public void updateUser(User user) {
        // Fetch the user from the database using their username or ID
        User existingUser = userRepository.findByUsername(user.getUsername());

        if (existingUser != null) {
            // Update only the fields that are not null
            if (user.getAddress() != null) {
                existingUser.setAddress(user.getAddress());
            }
            if (user.getContact() != null) {
                existingUser.setContact(user.getContact());
            }
            if (user.getEcontact() != null) {
                existingUser.setEcontact(user.getEcontact());
            }
            if (user.getPlate() != null) {
                existingUser.setPlate(user.getPlate());
            }
            if (user.getPassword() != null) {
                existingUser.setPassword(passwordEncoder.encode(user.getPassword()));
            }

            // Save the updated user
            userRepository.save(existingUser);
        }
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
	
 // Update the user's password
    public boolean updatePassword(String username, String newPassword) {
        User user = userRepository.findByUsername(username);
        if (user != null) {
            user.setPassword(newPassword);  // Encrypt the new password only once
            userRepository.save(user);  // Save the updated user object
            return true;
        }
        return false;
    }
    
    public boolean validateCurrentPassword(String username, String currentPassword) {
        User user = userRepository.findByUsername(username); // Retrieve the user from DB
        return passwordEncoder.matches(currentPassword, user.getPassword());  // Compare the entered password
    }
}
