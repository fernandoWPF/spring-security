package com.security.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter{

    @Autowired
    private UserService service;
    
    //Autorização: usuario/senha/permissoes
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(service).passwordEncoder(new BCryptPasswordEncoder());
    }
    
    //Autenticação: qual endpoint deve ou não ser autenticado
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
        .antMatchers(HttpMethod.GET, "/clientes").permitAll()//não será exigido autenticação para metodo GET no endpoint /clientes
        .antMatchers(HttpMethod.GET, "/clientes/*").permitAll()//não será exigido autenticação para metodo GET no endpoint /clientes/{id} por exemplo....
        .and().formLogin();//só para o spring criar uma tela de login automaticamente pra gente testar
    }

    //Autenticação: aqui configuramos os arquivos estáticos como css, js.... liberando acesso pra eles.
    @Override
    public void configure(WebSecurity web) throws Exception {
        //TO DO
    }
    
}
