package com.spotfinder.Controllers;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.spotfinder.Models.CustomUserDetails;
import com.spotfinder.Models.CustomUserDetailsService;
import com.spotfinder.Models.UserDto;

import jakarta.servlet.http.HttpSession;

@Controller
public class UserController {
	
	@Autowired
	private CustomUserDetailsService userDetailsService;
	
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
	
	@GetMapping("/map/backgate")
	public String backgateMap(Model model, Principal principal, HttpSession session) {
		CustomUserDetails user = userDetailsService.loadUserByUsername(principal.getName());
		model.addAttribute("user", user);
        session.setAttribute("user", user);
		return "backgate";
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
	    
	    model.addAttribute("user", user);
	    return "profile";
	}
	
	@GetMapping("/signout")
    public String signout() {
        return "redirect:/signin?signout";
    }
}
