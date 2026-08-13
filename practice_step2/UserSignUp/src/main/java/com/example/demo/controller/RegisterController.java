package com.example.demo.controller;

import org.apache.catalina.connector.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entity.Register;
import com.example.demo.service.AuthenticatedService;

@RestController
public class RegisterController {

	@Autowired
	AuthenticatedService authService;
	
	@PostMapping("/reg")
	public ResponseEntity<String>registerUser(@RequestBody Register register){
		
		String msg=authService.register(register);
		
		return new ResponseEntity<String>(msg,HttpStatus.CREATED);
		
		
	}
}
