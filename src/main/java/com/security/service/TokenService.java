package com.security.service;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.security.config.User;
import com.security.domain.dto.AuthenticationRequest;
import com.security.domain.dto.TokenDTO;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Service
public class TokenService {

	private static final String CLIENT_ID = "xpto123";
	
	@Autowired
	private AuthenticationManager authManager;
	
	public TokenDTO generateToken(AuthenticationRequest request) {
		UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
				request.getEmail(), request.getPassword());
		
		Authentication authentication = authManager.authenticate(authenticationToken);
		
		String token = getToken(authentication);
		
		return new TokenDTO.Builder()
				.withToken(token)
				.withTipo("Bearer")
				.build();
		
	}
	
	public boolean isValidToken(String token) {
		try {
			Jwts.parser().setSigningKey(CLIENT_ID).parseClaimsJws(token);
			//caso o token nao seja valido, p Jwts lancara uma exception
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	public String getUserIdByToken(String token) {
		Claims claims =  Jwts.parser().setSigningKey(CLIENT_ID).parseClaimsJws(token).getBody();
		return claims.getSubject();
	}

	private String getToken(Authentication authentication) {
		User user = (User) authentication.getPrincipal();
		Date hoje = new Date();
		return Jwts.builder()
				.setIssuer("API Rest")//quem chamou a geracao do Token. No caso, nossa API Rest
				.setSubject(user.getId())//um identificador desse usuario. Tem que ser algo que não se repete. Poderia ser o e-mail, mas usei o ID.
				.setIssuedAt(hoje)//setar uma data em que o token foi criado. Problema que ainda se trabalha com Date... Preciso ver um cast para LocalDate
				.setExpiration(new Date(hoje.getTime() + 86400))//tempo de expiracao do token
				//.signWith(alg, secretKey)alg: Algoritimo utilizado para gerar o token. secretKey: secret utilizado para gerar o Token. A especificacao de geracao de tokens precisa de um algoritimo e uma senha para assinatura, de modo a garantir a legitimidade do token.
				.signWith(SignatureAlgorithm.HS256, CLIENT_ID)
				.compact();
	}

}
