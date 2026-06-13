package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;

@RestController
public class controller {

	@Autowired
	JWTUtil jwtutil;
	@PostMapping("/genToken")
	public String craeteToken(@RequestBody Login login) {
		 System.out.println("Username = " + login.getUsername());
		String token=jwtutil.generateToken(login.getUsername());
		return token;
		
	}
	@GetMapping("/verify")
	public String verifyToken(@RequestHeader("Authorization") String authorization) {
		
		
		return jwtutil.ValidateToken(authorization);
		
		
		
		
	}
}
