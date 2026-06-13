package com.example.demo.Config;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.demo.entity.Register;
import com.example.demo.repository.RegisterRepo;
@Service
public class CustomUserDetailsService implements UserDetailsService{

	@Autowired
	RegisterRepo regRepo;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Register reg=regRepo.findByUsername(username);
		
	
		if(reg==null) {
			throw new UsernameNotFoundException("User not found");
		}


		return new User(
				reg.getUsername(),
				reg.getPassword(),
				new ArrayList<>()
		);
	}

}//UserDetails u=new User(reg.getUsername(),reg.getPassword(),new ArrayList<>());

//return u;
