package com.example.demo.Config;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.example.demo.entity.Register;
import com.example.demo.repository.RegisterRepo;

public class CustomUserDetailsService implements UserDetailsService{

	@Autowired
	RegisterRepo regRepo;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Register reg=regRepo.findByUsername(username);
		
		UserDetails u=new User(reg.getUsername(),reg.getPassword(),new ArrayList<>());
		
		return u;
	}

}
