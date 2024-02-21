package com.smart.contact.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.smart.contact.dao.UserRepository;
import com.smart.contact.helper.Message;
import com.smart.contact.model.User;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

import java.time.LocalDate;

@Controller
public class HomeController {

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder; 

	@GetMapping("/")
	public String home(Model model) {
		model.addAttribute("title", "Home | Samrt-Contact-Manager");
		return "home";
	}

	@GetMapping("/about")
	public String about(Model model) {
		model.addAttribute("title", "About | Samrt-Contact-Manager");
		return "about";
	}

	@GetMapping("/signup")
	public String signUp(Model model) {
		model.addAttribute("user", new User());
		model.addAttribute("title", "Register | Samrt-Contact-Manager");
		return "signup";
	}

	@PostMapping("/do_Register")
	public String signup( @Valid @ModelAttribute("user") User user,BindingResult result, Model model,HttpSession session) {
		try {
			if (!user.getTermAndCondition()) {
				throw new Exception("Please  Select Term And Condition!!!!");
			}
			if(result.hasErrors()) {
				System.out.println("Error "+result.toString());
				model.addAttribute("user",user);
				return "signup";
			}
			System.out.println(user.getTermAndCondition());
			System.out.println(user);
			user.setRole("ROLE_USER");
			user.setActive(false);
			user.setProfileUrl("profile.jpg");
			user.setJoinDate(LocalDate.now());
			user.setPassword(this.bCryptPasswordEncoder.encode(user.getPassword()));
			this.userRepository.save(user);
			model.addAttribute("user",new User());
            session.setAttribute("message",new Message("Successfully Register !!","success"));          
			return "signup";
		} catch (Exception e) {
			model.addAttribute("user", user);
            session.setAttribute("message",new Message("Something Went wrong !!","danger"));        
			e.printStackTrace();
			return "signup";
		}
	}

	@GetMapping("/signin")
	public String signIn(Model model) {
		model.addAttribute("title", "Login | Samrt-Contact-Manager");
		return "signin";
	}

}