package com.security.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRespository repository;

    @Override
    public UserDetails loadUserByUsername(String username) {
        User user = repository.findByNome(username).orElseThrow(RuntimeException::new);
        return user;
    }

}
