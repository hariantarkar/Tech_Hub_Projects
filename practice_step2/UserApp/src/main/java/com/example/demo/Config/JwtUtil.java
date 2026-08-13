package com.example.demo.Config;

import java.awt.RenderingHints.Key;
import java.util.Date;

import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtil {

	private String ScecerKey = "HariAntarkar@1234HariAntarkar@1234HariAntarkar@1234";

	private java.security.Key key = Keys.hmacShaKeyFor(ScecerKey.getBytes());


	
	public String generateToken(String username) {
		System.out.println("Generating token for = " + username);
		return Jwts.builder().setSubject(username).setIssuedAt(new Date())
				.setExpiration(new Date(System.currentTimeMillis() + 10000)).signWith(key).compact();
	}
	
	public String verifyToken(String token) {

	    if(token == null || !token.startsWith("Bearer ")) {
	        throw new RuntimeException("Invalid token");
	    }

	    String newToken = token.substring(7);

	    try {

	        Claims claim = Jwts.parserBuilder()
	                .setSigningKey(key)
	                .build()
	                .parseClaimsJws(newToken)
	                .getBody();

	        System.out.println("Username = " + claim.getSubject());

	        return claim.getSubject();

	    } catch(io.jsonwebtoken.ExpiredJwtException e) {

	        throw new RuntimeException("Token expired");

	    } catch(Exception e) {

	        throw new RuntimeException("Invalid token");

	    }
	}
}
/*
 * public String verifyToken(String token) { if(token==null
 * ||!token.startsWith("Bearer ")) {
 * 
 * throw new RuntimeException("invalid token"); }
 * 
 * String newToken=token.substring(7);
 * 
 * 
 * 
 * System.out.println("JWT = " + newToken);
 * 
 * Claims claim=Jwts.parserBuilder() .setSigningKey(key) .build()
 * .parseClaimsJws(newToken) .getBody(); System.out.println("Username = " +
 * claim.getSubject());
 * 
 * return claim.getSubject(); }
 */

/*
 * return Jwts .parserBuilder() .setSigningKey(key) .build()
 * .parseClaimsJwt(newToken) .getBody() .getSubject();
 */



/*
 * public String generateToken(String token) {
 * 
 * return Jwts .builder() .setSubject(token) .signWith(key) .compact();
 * 
 * }
 */