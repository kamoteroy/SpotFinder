package com.spotfinder.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.spotfinder.Models.UserDto;
import com.spotfinder.Models.UserService;

@Controller
public class RegisterController {
	
	@Autowired
	private UserService userService;
	
	@GetMapping("/signup")
	public String signup(Model model, UserDto userDto) {
	    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	    if (authentication != null && authentication.isAuthenticated() 
	        && !(authentication instanceof AnonymousAuthenticationToken)) {
	        return "redirect:/parking"; 
	    }
	    model.addAttribute("user", userDto);
	    return "signup";
	}
	
	@PostMapping("/signup")
	public String signup(@ModelAttribute("user") UserDto userDto, Model model) {
	    if (userService.existsByUsername(userDto.getUsername())) {
	        model.addAttribute("usernameError", "IDnum already exists.");
	        return "signup";
	    }

	    if (userService.existsByEmail(userDto.getEmail())) {
	        model.addAttribute("emailError", "Email already exists.");
	        return "signup";
	    }
	    
	    if (!userDto.getPassword().equals(userDto.getCpassword())) {
	        model.addAttribute("passwordError", "Passwords do not match.");
	        return "signup";
	    }
	    userService.save(userDto);
	    model.addAttribute("registerSuccess", "Successful");
	    return "redirect:/signup?success";
	}

}
