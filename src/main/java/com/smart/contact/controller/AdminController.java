package com.smart.contact.controller;

import com.smart.contact.dao.ContactRepository;
import com.smart.contact.dao.UserRepository;
import com.smart.contact.model.LoggedInUsers;
import com.smart.contact.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Controller
@RequestMapping("/admin")
public class AdminController {

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private ContactRepository contactRepository;

	@Autowired
	private LoggedInUsers loggedInUsers;
	@ModelAttribute
	public void addCommanData(Model model, Principal principal) {
		String userName = principal.getName();
		System.out.println(userName);

		// get User Detail By UserName(EmailId)
		User user = this.userRepository.getUserByUserName(userName);
		model.addAttribute("user",user);
	}
	@GetMapping("/index")
	public String adminDashboard(Model model) {
		model.addAttribute("title", "Admin-Dashboard| Samrt-Contact-Manager");
		List<User> list=new ArrayList<>();
		Set<String> username = this.loggedInUsers.getUsers();
		for (String name: username) {
			User userByUserName = this.userRepository.getUserByUserName(name);
			list.add(userByUserName);
		}

          model.addAttribute("users",list);

		long countUser = this.userRepository.count();
		model.addAttribute("numberOfUser",countUser);
		long countConatct = this.contactRepository.count();
		model.addAttribute("numberOfContact",countConatct);
		return "admin/admin_dashboard";
	}

	@GetMapping("/show-users")
	public String showUsers(Model model){
		List<User> users = this.userRepository.findAll();
		model.addAttribute("users",users);
		return "admin/show-users";
	}



	
}
