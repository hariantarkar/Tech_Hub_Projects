package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.security.core.Authentication;
import com.example.demo.Config.JwtUtil;
import com.example.demo.entity.Register;
import com.example.demo.repository.RegisterRepo;


@Service("authService")
public class AuthenticatedService {

	@Autowired
	private RegisterRepo regRepo;
	
	@Autowired
	private PasswordEncoder  passwordEncoder;
	
	@Autowired
	private JwtUtil jwtUtil;
	
	public String register(Register reg) {
		
		reg.setPassword(passwordEncoder.encode(reg.getPassword()));
		Register register=regRepo.save(reg);
		
		return register!=null?"save":"not save";
		
	}
	@Autowired
	private AuthenticationManager authenticationManager;
	
	public String login(String username,String password){

		System.out.println("Login username = "+username);
		System.out.println("Login password = "+password);


		Authentication authentication =
		        authenticationManager.authenticate(
		        new UsernamePasswordAuthenticationToken(
		                username,
		                password
		        ));


		System.out.println("Authentication = "+authentication.isAuthenticated());


		return jwtUtil.generateToken(username);

		}

}
