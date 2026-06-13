package com.example.demo;

import java.security.Key;

import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.util.*;

import javax.management.RuntimeErrorException;

@Component
public class JWTUtil {

	private String ScecerKey = "HariAntarkar@1234HariAntarkar@1234HariAntarkar@1234";

	private Key key = Keys.hmacShaKeyFor(ScecerKey.getBytes());

	public String generateToken(String username) {
		System.out.println("Generating token for = " + username);
		return Jwts.builder().setSubject(username).setIssuedAt(new Date())
				.setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60)).signWith(key).compact();
	}

	public String ValidateToken(String token) {

		if (token == null || !token.startsWith("Bearer ")) {
			throw new RuntimeException("Invalid authorization header... ");

		}
		String newToken = token.substring(7);
		System.out.println("JWT = " + newToken);
		
		 Claims claim=Jwts.parserBuilder() 
				 .setSigningKey(key) 
				 .build()
				 .parseClaimsJws(newToken) 
				 .getBody();
		 System.out.println("Username = " + claim.getSubject());
		  
		  return claim.getSubject();
		 

	}
}
