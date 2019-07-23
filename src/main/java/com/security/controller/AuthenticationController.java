package com.security.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.security.domain.dto.AuthenticationRequest;
import com.security.domain.dto.TokenDTO;
import com.security.service.TokenService;

@RestController
public class AuthenticationController {

	@Autowired
	private TokenService authService;
	
	@PostMapping("/oauth")
	public ResponseEntity<TokenDTO> authentication(@RequestBody AuthenticationRequest authentication){
		
		TokenDTO token = authService.generateToken(authentication);
		
		return ResponseEntity.ok().body(token);
	}
	
	
}
