package com.youbook.YouBook.security.models;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

public class MyUser extends User {
    private final Long id;
    private final Boolean is_active;
    private final String name;

    public MyUser(String username, String password, Collection<? extends GrantedAuthority> authorities, Long id, Boolean is_active, String name) {
        super(username, password, authorities);
        this.id = id;
        this.is_active = is_active;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public Boolean getIs_active() {
        return is_active;
    }

    public String getName() {
        return name;
    }
}
