package com.security.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.security.filter.AutenticacaoViaTokenFilter;
import com.security.service.TokenService;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter{

    @Autowired
    private UserService service;
    @Autowired
    private TokenService tokenService;
    @Autowired
    private UserRespository userRepository;
    
    //Autorizacao: usuario/senha/permissoes
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(service).passwordEncoder(new BCryptPasswordEncoder());
    }
    
    //Autenticacao: qual endpoint deve ou nao ser autenticado
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
        .antMatchers(HttpMethod.GET, "/clientes").permitAll()//nao sera exigido autenticaco para metodo GET no endpoint /clientes
        .antMatchers(HttpMethod.GET, "/clientes/*").permitAll()//nao sera exigido autenticacao para metodo GET no endpoint /clientes/{id} por exemplo....
        .antMatchers(HttpMethod.POST, "/oauth").permitAll()//liberando acesso para solicitar token
        .anyRequest().authenticated()//para que todas os demais endpoints sejam autenticados
        .and().csrf().disable()
        .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)//para dizer que as sessoes das requisicoes serao todas stateless,
        //isto eh, nao vamos guardar nenhuma informacao sobre sessoes. Isso eh um principio do REST
        .and().addFilterBefore(new AutenticacaoViaTokenFilter(tokenService, userRepository), UsernamePasswordAuthenticationFilter.class);//estou dizendo ao Spring para executar primeiramete o meu filtro e depois executar o dele. Isso para que nao possamos validar nosso token.
    }

    //Autenticacao: aqui configuramos os arquivos estáticos como css, js.... liberando acesso pra eles.
    @Override
    public void configure(WebSecurity web) throws Exception {
        //TO DO
    }

    //para que seja possivel fazer a injecao de dependencia, pois sem isso o spring nao consegue cria-lo
    @Override
    @Bean
    protected AuthenticationManager authenticationManager() throws Exception {
    	return super.authenticationManager();
    }
}
