package com.example.user_service.service;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.example.user_service.repository.entity.UserEntity;

public class CustomUserDetails implements UserDetails {
	
	private String userEmail;
    private String userPassword;
    private Collection<? extends GrantedAuthority> authorities;
    private List<String> roles; // Store the roles

    public CustomUserDetails(UserEntity user, List<String> roles) {
        this.userEmail = user.getUserEmail();
        this.userPassword = user.getUserPassword();
        this.roles = roles;  // Store the roles
        this.authorities = roles.stream()
                                .map(SimpleGrantedAuthority::new)
                                .collect(Collectors.toList());
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return userPassword;
    }

    @Override
    public String getUsername() {
        return userEmail;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    // Getter method to retrieve the roles
    public List<String> getRoles() {
        return roles;
    }
}
