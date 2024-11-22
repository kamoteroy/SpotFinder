package com.spotfinder.Controllers;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.spotfinder.Models.CustomUserDetails;
import com.spotfinder.Models.CustomUserDetailsService;
import com.spotfinder.Models.User;
import com.spotfinder.Models.UserDto;
import com.spotfinder.Models.UserService;

import jakarta.servlet.http.HttpSession;

@Controller
public class UserController {
	
	@Autowired
	private CustomUserDetailsService userDetailsService;
	private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }
	
	@GetMapping("/history")
	public String history(Model model, UserDto userDto) {
		model.addAttribute("user", userDto);
		return "history";
	}
	
	@GetMapping("/map")
	public String parking(Model model, Principal principal, HttpSession session) {
		CustomUserDetails user = userDetailsService.loadUserByUsername(principal.getName());
		model.addAttribute("user", user);
        session.setAttribute("user", user);
		return "map";
	}
	
	/*@GetMapping("/map/backgate")
	public String backgateMap(Model model, Principal principal, HttpSession session) {
		CustomUserDetails user = userDetailsService.loadUserByUsername(principal.getName());
		model.addAttribute("user", user);
        session.setAttribute("user", user);
		return "backgate";
	}*/
	
	@GetMapping("/map/open-area")
	public String open_areaMap(Model model, Principal principal, HttpSession session) {
		CustomUserDetails user = userDetailsService.loadUserByUsername(principal.getName());
		model.addAttribute("user", user);
        session.setAttribute("user", user);
		return "oarea";
	}
	@GetMapping("/map/phys")
	public String physMap(Model model, Principal principal, HttpSession session) {
		CustomUserDetails user = userDetailsService.loadUserByUsername(principal.getName());
		model.addAttribute("user", user);
        session.setAttribute("user", user);
		return "phys";
	}
	
	@GetMapping("/map/GLE")
	public String GLEMap(Model model, Principal principal, HttpSession session) {
		CustomUserDetails user = userDetailsService.loadUserByUsername(principal.getName());
		model.addAttribute("user", user);
	    session.setAttribute("user", user);
		return "GLE";
	}
	
	@GetMapping("/profile")
	public String profile(HttpSession session, Model model) {
	    CustomUserDetails user = (CustomUserDetails) session.getAttribute("user");

	    if (user == null) {
	        model.addAttribute("error", "User not found in session. Please log in.");
	        return "signin";
	    }

	    // Add the user to the model to be used in the profile page
	    model.addAttribute("user", user);
	    return "profile";
	}

	
	@PostMapping("/updateProfile")
	public String updateUser(@ModelAttribute("user") User user, HttpSession session, RedirectAttributes redirectAttributes) {
	    try {
	        // Fetch the current user from the session
	        CustomUserDetails currentUser = (CustomUserDetails) session.getAttribute("user");

	        if (currentUser == null) {
	            redirectAttributes.addFlashAttribute("error", "User not found in session. Please log in.");
	            return "redirect:/signin";  // Redirect to login if no user is found in session
	        }

	        // Update the user object with the new data
	        // Here, we assume you have a service method to update the user in the database
	        userService.updateUser(user);

	        // Update the session with the new user data
	        currentUser.setAddress(user.getAddress());
	        currentUser.setContact(user.getContact());
	        currentUser.setEcontact(user.getEcontact());

	        // Save the updated user back to the session
	        session.setAttribute("user", currentUser);

	        // Add a success message and redirect back to profile page
	        redirectAttributes.addFlashAttribute("message", "Your profile has been successfully updated.");
	    } catch (Exception e) {
	        // In case of an error, add an error message
	        redirectAttributes.addFlashAttribute("error", "Error updating your profile. Please try again.");
	    }

	    // Redirect to profile page
	    return "redirect:/profile";
	}

	
	@GetMapping("/signout")
    public String signout() {
        return "redirect:/signin?signout";
    }
}
