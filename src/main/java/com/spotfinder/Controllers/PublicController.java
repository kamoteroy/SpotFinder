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

import com.spotfinder.Models.ContactUs;
import com.spotfinder.Models.ContactUsService;
import com.spotfinder.Models.CustomUserDetails;
import com.spotfinder.Models.UserDto;
import com.spotfinder.Models.UserService;

import jakarta.servlet.http.HttpSession;

@Controller
public class PublicController {
	
	@Autowired
	private UserService userService;

	public PublicController(UserService userService) { this.userService = userService; }
	
	@Autowired
    private ContactUsService contactUsService;


	@GetMapping("/")
	public String index(Model model, UserDto userDto) {
	    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	    if (authentication != null && authentication.isAuthenticated() 
	        && !(authentication instanceof AnonymousAuthenticationToken)) {
	        return "redirect:/map"; 
	    }
	    model.addAttribute("user", userDto);
	    return "index";
	}
	
	@GetMapping("/socials")
	public String socials(Model model, HttpSession session) {
		CustomUserDetails user = (CustomUserDetails) session.getAttribute("user");
		model.addAttribute("user", user);
	    return "socials";
	}
	
	@GetMapping("/about")
	public String about(Model model, HttpSession session) {
		CustomUserDetails user = (CustomUserDetails) session.getAttribute("user");
		model.addAttribute("user", user);
	    return "about";
	}
	
    @GetMapping("/contact")
    public String showContactForm(Model model, HttpSession session) {
    	CustomUserDetails user = (CustomUserDetails) session.getAttribute("user");
		model.addAttribute("user", user);
        model.addAttribute("contact", new ContactUs());
        return "contactus";
    }

    @PostMapping("/contactus")
    public String submitContactForm(ContactUs contact) {
        contactUsService.save(contact);
        return "redirect:/thank-you";
    }
	
	@GetMapping("/signin")
	public String signin(Model model, UserDto userDto) {
	    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	    if (authentication != null && authentication.isAuthenticated() 
	        && !(authentication instanceof AnonymousAuthenticationToken)) {
	        return "redirect:/map"; 
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
	 
	    return "redirect:/map";
	}
	
	@GetMapping("/forgot")
	public String forgot(Model model, UserDto userDto) {
	    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	    if (authentication != null && authentication.isAuthenticated() 
	        && !(authentication instanceof AnonymousAuthenticationToken)) {
	        return "redirect:/map"; 
	    }
	    model.addAttribute("user", userDto);
	    return "forgot";
	}
}
