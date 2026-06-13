package com.example.demo.controller;

import org.apache.catalina.connector.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.LoginRequest.LoginRequest;
import com.example.demo.entity.Register;
import com.example.demo.service.AuthenticatedService;

@RestController
public class RegisterController {

	@Autowired
	AuthenticatedService authService;
	
	@PostMapping("/auth/reg")
	public ResponseEntity<String>registerUser(@RequestBody Register register){
		
		String msg=authService.register(register);
		
		return new ResponseEntity<String>(msg,HttpStatus.CREATED);
		
		
	}
	@PostMapping("/auth/login")
	public ResponseEntity<String> login(
			@RequestBody LoginRequest request){


		String token = authService.login(
				request.getUsername(),
				request.getPassword()
		);


		return new ResponseEntity<>(token,HttpStatus.OK);

	}
	@GetMapping("/user/profile")
	public String profile() {
	    return "Welcome User";
	}
}
