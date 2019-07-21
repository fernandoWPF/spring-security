package com.security.config;

import org.springframework.security.core.GrantedAuthority;

public class Permissao implements GrantedAuthority {

    private static final long serialVersionUID = 1L;

    private String permissao;

    @Override
    public String getAuthority() {
        return permissao;
    }

}
