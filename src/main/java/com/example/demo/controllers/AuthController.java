package com.example.demo.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.LoginRequestDTO;
import com.example.demo.response.ResponseAPI;
import com.example.demo.security.CustomUserDetailsService;
import com.example.demo.security.JwtService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
	
	private final AuthenticationManager authenticationManager;
	
	private final CustomUserDetailsService userDetailsService;
	
	private final JwtService jwtService;
	
	public AuthController(
			AuthenticationManager authenticationManager,
			CustomUserDetailsService userDetailsService,
			JwtService jwtService) {
		
		this.authenticationManager = authenticationManager;
		
		this.userDetailsService = userDetailsService;
		
		this.jwtService = jwtService;
	}
	
	@PostMapping("/login")
	public ResponseEntity<ResponseAPI<String>> login(@RequestBody LoginRequestDTO request) {
		
		authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
		);
		
		UserDetails userDetails = userDetailsService.loadUserByUsername(request.getUsername());
		
		String token = jwtService.generateToken(userDetails);
		
		return ResponseEntity.ok(ResponseAPI.success(token));
	}
		
}
	

