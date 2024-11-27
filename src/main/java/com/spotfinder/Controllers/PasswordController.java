package com.spotfinder.Controllers;

import com.spotfinder.Models.CustomUserDetails;
import com.spotfinder.Models.CustomUserDetailsService;
import com.spotfinder.Models.ParkingSlotRepository;
import com.spotfinder.Models.User;
import com.spotfinder.Models.UserService;
import com.spotfinder.Models.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpSession;

@Controller
public class PasswordController {
	
	private final UserService userService;
	@Autowired
	PasswordEncoder passwordEncoder;
	
	@Autowired
    public PasswordController(UserService userService) {
        this.userService = userService;
    }

	@GetMapping("/changepass")
	public String changePass(HttpSession session, Model model) {
	    CustomUserDetails user = (CustomUserDetails) session.getAttribute("user");

	    if (user == null) {
	        model.addAttribute("error", "User not found in session. Please log in.");
	        return "signin";
	    }

	    // Add the user to the model to be used in the profile page
	    model.addAttribute("user", user);
	    return "changepass";
	}
	
	@GetMapping("/newpass")
	public String newpass(HttpSession session, Model model) {
	    CustomUserDetails user = (CustomUserDetails) session.getAttribute("user");

	    if (user == null) {
	        model.addAttribute("error", "User not found in session. Please log in.");
	        return "signin";
	    }

	    // Add the user to the model to be used in the profile page
	    model.addAttribute("user", user);
	    return "newpass";
	}
	
	@PostMapping("/changepassword")
	public String changePassword(@ModelAttribute("user") User user, HttpSession session, RedirectAttributes redirectAttributes) {
	    try {
	        // Fetch the current user from the session
	        CustomUserDetails currentUser = (CustomUserDetails) session.getAttribute("user");
	        if (currentUser == null) {
	            redirectAttributes.addFlashAttribute("error", "User not found in session. Please log in.");
	            return "redirect:/signin";  // Redirect to login if no user is found in session
	        }

	        // Get the username from the current user
	        String username = currentUser.getUsername();

	        // Fetch the user details from the database using the username
	        User dbUser = userService.findByUsername(username);

	        // Check if the provided old password matches the current password in the database
	        if (!passwordEncoder.matches(user.getPassword(), dbUser.getPassword())) {  // Using passwordEncoder to compare hashed passwords
	            // If password is incorrect, add error message and stay on the changepass page
	            redirectAttributes.addFlashAttribute("error", "Wrong Password!");
	            return "redirect:/changepass";  // Redirect back to the change password page with error message
	        }

	        // If password matches, redirect to the reset password page
	        return "newpass";  // Redirect to reset password page
	    } catch (Exception e) {
	        // In case of an error, add an error message
	        redirectAttributes.addFlashAttribute("error", "Error processing your request. Please try again.");
	    }

	    // Redirect back to change password page in case of error
	    return "redirect:/changepass";
	}

	@PostMapping("/newpassword")
	public String newPassword(@ModelAttribute("user") User user, HttpSession session, RedirectAttributes redirectAttributes, Model model) {
	    try {
	        // Fetch the current user from the session
	        CustomUserDetails currentUser = (CustomUserDetails) session.getAttribute("user");
	        if (currentUser == null) {
	            redirectAttributes.addFlashAttribute("error", "User not found in session. Please log in.");
	            return "redirect:/signin";  // Redirect to login if no user is found in session
	        }

	        // Get the username from the current user
	        String username = currentUser.getUsername();

	        // Fetch the user details from the database using the username
	        User dbUser = userService.findByUsername(username);
	        
	        // Debug prints to check the passwords
	        System.out.println(dbUser.getPassword());
	        System.out.println(user.getPassword());

	        // Check if the provided password matches the current password in the database
	        if (passwordEncoder.matches(user.getPassword(), dbUser.getPassword())) {  // Using passwordEncoder to compare hashed passwords
	            // If password is same, add error message and stay on the new password page
	            redirectAttributes.addFlashAttribute("error", "Password is the same as old password");
	            return "redirect:/newpass";  // Redirect back to the change password page
	        }

	        // If password doesn't match, encode the new password and update the user
	        String encodedNewPassword = passwordEncoder.encode(user.getPassword());  // Encode the new password

	        // Set the encoded new password in the user object
	        user.setPassword(encodedNewPassword);

	        // Proceed with updating the user (the password is now encoded)
	        userService.updatePassword(username, user.getPassword());  // Pass the encoded password correctly

	        // Update the session with the new user data (including the updated password)
	        currentUser.setPassword(encodedNewPassword);  // Set the new encoded password in the session
	        session.setAttribute("user", currentUser);

	        // Add a success message and redirect back to the profile page
	        redirectAttributes.addFlashAttribute("success", "Your password has been successfully updated.");
	        return "redirect:/profile";  // Redirect to profile page or wherever you want

	    } catch (Exception e) {
	        // In case of an error, add an error message
	        redirectAttributes.addFlashAttribute("error", "Error updating your password. Please try again.");
	        return "redirect:/newpass";  // Redirect back to the password change page in case of error
	    }
	}



}