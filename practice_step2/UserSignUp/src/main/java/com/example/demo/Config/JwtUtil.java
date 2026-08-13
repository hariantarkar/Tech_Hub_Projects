package com.example.demo.Config;

import java.awt.RenderingHints.Key;

import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtil {

	private String ScecerKey = "HariAntarkar@1234HariAntarkar@1234HariAntarkar@1234";

	private java.security.Key key = Keys.hmacShaKeyFor(ScecerKey.getBytes());

	
	
	
	public String generateToken(String token) {
		
		return Jwts
				.builder()
				.setSubject(token)
				.signWith(key)
				.compact();
		
	}
	public String verifyToken(String token) {
		if(token==null ||!token.startsWith("Bearer ")) {
			
		throw new RuntimeException("invalid token");
		}
	
	String newToken=token.substring(7);
	
	
	return Jwts
			.parserBuilder()
			.setSigningKey(key)
			.build()
			.parseClaimsJwt(newToken)
			.getBody()
			.getSubject();
	
	}
}
