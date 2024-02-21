package com.smart.contact.configration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.smart.contact.dao.UserRepository;
import com.smart.contact.model.User;

public class UserDetailServiceImple implements UserDetailsService{

	@Autowired
	private UserRepository repository;
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User userByUserName = this.repository.getUserByUserName(username);
		if(userByUserName == null) {
			throw new UsernameNotFoundException("could Not Found !!! ");
		}
		CustomUserDetail customUserDetail=new CustomUserDetail(userByUserName);
		return customUserDetail;
	}


}
