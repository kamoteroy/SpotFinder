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
public class PublicController {
	
	@Autowired
	private UserService userService;

	public PublicController(UserService userService) { this.userService = userService; }

	@GetMapping("/")
	public String index(Model model, UserDto userDto) {
	    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	    if (authentication != null && authentication.isAuthenticated() 
	        && !(authentication instanceof AnonymousAuthenticationToken)) {
	        return "redirect:/parking"; 
	    }
	    model.addAttribute("user", userDto);
	    return "index";
	}
	
	@GetMapping("/signin")
	public String signin(Model model, UserDto userDto) {
	    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	    if (authentication != null && authentication.isAuthenticated() 
	        && !(authentication instanceof AnonymousAuthenticationToken)) {
	        return "redirect:/parking"; 
	    }
	    model.addAttribute("user", userDto);
	    return "signin";
	}

	@PostMapping("/signin") //Not Working if .loginPage() is set
	public String signin(@ModelAttribute("user") UserDto userDto, Model model) {
		
	    if (!userService.existsByUsername(userDto.getUsername())) {
	        model.addAttribute("loginError", "Username does not exist.");
	        return "signin";
	    }
	    if (!userService.isPasswordValid(userDto.getUsername(), userDto.getPassword())) {
	        model.addAttribute("loginError", "Incorrect password.");
	        return "signin";
	    }
	 
	    return "redirect:/parking";
	}
	
	@GetMapping("/forgot")
	public String forgot(Model model, UserDto userDto) {
	    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	    if (authentication != null && authentication.isAuthenticated() 
	        && !(authentication instanceof AnonymousAuthenticationToken)) {
	        return "redirect:/parking"; 
	    }
	    model.addAttribute("user", userDto);
	    return "forgot";
	}
}
