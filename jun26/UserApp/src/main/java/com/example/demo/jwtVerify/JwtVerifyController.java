package com.example.demo.jwtVerify;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.Config.JwtUtil;
import com.example.demo.entity.Register;
@RestController
public class JwtVerifyController {

	@Autowired
	JwtUtil jwtutil;
	@PostMapping("/auth/genToken")
	public String craeteToken(@RequestBody Register register) {
		 System.out.println("Username = " + register.getUsername());
		String token=jwtutil.generateToken(register.getUsername());
		return token;
	}
	
	@GetMapping("/auth/verify")
	public String verifyToken(@RequestHeader("Authorization") String authorization) {
		
		
		return jwtutil.verifyToken(authorization);
		
		
		
	}		
}
