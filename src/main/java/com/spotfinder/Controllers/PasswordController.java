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
	    // Check if the password has been verified in the session
	    Boolean passwordVerified = (Boolean) session.getAttribute("passwordVerified");
	    if (passwordVerified == null || !passwordVerified) {
	        // If password has not been verified, redirect to the change password page
	        return "redirect:/changepass";  // User must go through the change password process first
	    }

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
	        if (!passwordEncoder.matches(user.getPassword(), dbUser.getPassword())) {
	            // If password is incorrect, add error message and stay on the changepass page
	            redirectAttributes.addFlashAttribute("error", "Wrong Password!");
	            return "redirect:/changepass";  // Redirect back to the change password page with error message
	        }

	        // If password matches, set a session attribute allowing access to the new password page
	        session.setAttribute("passwordVerified", true);

	        // Redirect to the reset password page
	        return "redirect:/newpass";  // Redirect to reset password page
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

	        // Debug prints to check the passwords (you might want to remove these after testing)
	        System.out.println("Current password (db): " + dbUser.getPassword());
	        System.out.println("New password (user input): " + user.getPassword());

	        // Check if the new password matches the old password (you don't want them to be the same)
	        if (passwordEncoder.matches(user.getPassword(), dbUser.getPassword())) {
	            // If the new password is the same as the old password, add an error message
	            redirectAttributes.addFlashAttribute("error", "New password cannot be the same as the old password.");
	            return "redirect:/newpass";  // Redirect back to the new password page with an error message
	        }

	        // If the new password is different from the old password, encode it
	        String encodedNewPassword = passwordEncoder.encode(user.getPassword());

	        // Proceed with updating the user's password in the database
	        userService.updatePassword(username, encodedNewPassword);  // Ensure that the password is updated with the encoded version

	        // Update the session with the new user object (this includes the updated password)
	        currentUser.setPassword(encodedNewPassword);  // Set the new encoded password in the session object
	        session.setAttribute("user", currentUser);  // Update session with the new password

	        // Add a success message and redirect to the profile page
	        redirectAttributes.addFlashAttribute("success", "Your password has been successfully updated.");
	        return "redirect:/profile";  // Redirect to profile page or wherever you want

	    } catch (Exception e) {
	        // Handle any exceptions and show an error message
	        redirectAttributes.addFlashAttribute("error", "Error updating your password. Please try again.");
	        return "redirect:/newpass";  // Redirect back to the new password page in case of an error
	    }
	}




}