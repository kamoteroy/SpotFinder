package com.spotfinder.Controllers;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.spotfinder.Models.CustomUserDetails;
import com.spotfinder.Models.CustomUserDetailsService;
import com.spotfinder.Models.ParkingSlot;
import com.spotfinder.Models.ParkingSlotRepository;
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
    private ParkingSlotRepository parkingSlotRepository;

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
		List<ParkingSlot> parkingSlots = parkingSlotRepository.findAll();

        // Filter and sort parking slots that start with 'P' (case-insensitive)
        List<ParkingSlot> PSortedSlots = parkingSlots.stream()
            .filter(slot -> slot.getSlotName().toLowerCase().startsWith("p"))
            .sorted((slot1, slot2) -> slot1.getSlotName().compareTo(slot2.getSlotName())) // Sort by slotName alphabetically
            .collect(Collectors.toList());
        // Count how many parking slots are occupied (occupy = true)
        long PoccupiedCount = PSortedSlots.stream()
            .filter(ParkingSlot::isOccupy) // Count occupied slots (occupy == true)
            .count();

        // Add both the sorted and filtered parking slots and the occupied count to the model
        model.addAttribute("PSlots", PSortedSlots);
        model.addAttribute("PoccupiedCount", PoccupiedCount);

        // Filter and sort parking slots that start with 'P' (case-insensitive)
        List<ParkingSlot> GSortedSlots = parkingSlots.stream()
            .filter(slot -> slot.getSlotName().toLowerCase().startsWith("g"))
            .sorted((slot1, slot2) -> slot1.getSlotName().compareTo(slot2.getSlotName())) // Sort by slotName alphabetically
            .collect(Collectors.toList());
        // Count how many parking slots are occupied (occupy = true)
        long GoccupiedCount = GSortedSlots.stream()
            .filter(ParkingSlot::isOccupy) // Count occupied slots (occupy == true)
            .count();

        // Add both the sorted and filtered parking slots and the occupied count to the model
        model.addAttribute("GSlots", GSortedSlots);
        model.addAttribute("GoccupiedCount", GoccupiedCount);
        
        // Filter and sort parking slots that start with 'P' (case-insensitive)
        List<ParkingSlot> OSortedSlots = parkingSlots.stream()
            .filter(slot -> slot.getSlotName().toLowerCase().startsWith("o"))
            .sorted((slot1, slot2) -> slot1.getSlotName().compareTo(slot2.getSlotName())) // Sort by slotName alphabetically
            .collect(Collectors.toList());
        // Count how many parking slots are occupied (occupy = true)
        long OoccupiedCount = OSortedSlots.stream()
            .filter(ParkingSlot::isOccupy) // Count occupied slots (occupy == true)
            .count();

        // Add both the sorted and filtered parking slots and the occupied count to the model
        model.addAttribute("OSlots", OSortedSlots);
        model.addAttribute("OoccupiedCount", OoccupiedCount);
        
     // Filter and sort parking slots that start with 'P' (case-insensitive)
        List<ParkingSlot> BSortedSlots = parkingSlots.stream()
            .filter(slot -> slot.getSlotName().toLowerCase().startsWith("b"))
            .sorted((slot1, slot2) -> slot1.getSlotName().compareTo(slot2.getSlotName())) // Sort by slotName alphabetically
            .collect(Collectors.toList());
        // Count how many parking slots are occupied (occupy = true)
        long BoccupiedCount = BSortedSlots.stream()
            .filter(ParkingSlot::isOccupy) // Count occupied slots (occupy == true)
            .count();

        // Add both the sorted and filtered parking slots and the occupied count to the model
        model.addAttribute("BSlots", BSortedSlots);
        model.addAttribute("BoccupiedCount", BoccupiedCount);

        // Filter the list where 'occupy' is true and count how many slots are occupied
        long occupiedCount = parkingSlots.stream()
            .filter(ParkingSlot::isOccupy)  // Filters parking slots where 'occupy' is true
            .count();  // Counts the number of elements after filtering
        
        model.addAttribute("occupiedCount", occupiedCount);
		
		model.addAttribute("parkingSlots", parkingSlots);
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
	}
	@GetMapping("/map/GLE")
	public String GLEMap(Model model, Principal principal, HttpSession session) {
		CustomUserDetails user = userDetailsService.loadUserByUsername(principal.getName());
		model.addAttribute("user", user);
	    session.setAttribute("user", user);
		return "GLE";
	}
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
	}*/
	
	
	
	
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
	        // Assuming the userService.updateUser() handles password hashing if needed
	        userService.updateUser(user);

	        // Update the session with the new user data (address, contact, etc.)
	        currentUser.setAddress(user.getAddress());
	        currentUser.setContact(user.getContact());
	        currentUser.setEcontact(user.getEcontact());
	        currentUser.setPlate(user.getPlate());  // Set the new plate number

	        // Save the updated user back to the session
	        session.setAttribute("user", currentUser);

	        // Add a success message and redirect back to the profile page
	        redirectAttributes.addFlashAttribute("success", "Your profile has been successfully updated.");
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
