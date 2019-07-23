package com.security.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import com.security.config.User;
import com.security.config.UserRespository;
import com.security.service.TokenService;

public class AutenticacaoViaTokenFilter extends OncePerRequestFilter {

	private TokenService service;
	private UserRespository userRespository;
	
	public AutenticacaoViaTokenFilter(TokenService service, UserRespository userRespository) {
		this.service = service;
		this.userRespository = userRespository;
	}
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		String token = getToken(request);
		
		boolean isValid = service.isValidToken(token);
		
		if(isValid) {
			autenticar(token);
		}
		
		filterChain.doFilter(request, response);// essa linha diz ao Spring: ja validei, pode seguir agora...

	}

	private void autenticar(String token) {
		
		String id = service.getUserIdByToken(token);//pego o ususario que setei o id dentro do token
		
		User user = userRespository.findById(id).orElseThrow(RuntimeException::new);//busco o usuario por id
		
		//aqui eu crio um autentication para passar ao spring. Nao preciso passar senha, pois a validacao via senha ja foi feita na geracao do token
		UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
		
		//falo pro Spring que ele pode autenticar
		SecurityContextHolder.getContext().setAuthentication(authentication);
		
	}

	private String getToken(HttpServletRequest request) {
		String token = request.getHeader("Authorization");

		if (StringUtils.isBlank(token)) {
			return null;
		}

		return token.substring(7, token.length());
	}

}
