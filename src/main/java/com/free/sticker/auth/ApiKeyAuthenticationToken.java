package com.free.sticker.auth;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class ApiKeyAuthenticationToken  implements Authentication {
    private final String apiKey;
    private boolean authenticated = true;
    private Object details; // Field for additional details

    public ApiKeyAuthenticationToken(String apiKey) {
        this.apiKey = apiKey;
    }

    @Override
    public String getName() {
        return apiKey;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null; // No authorities assigned
    }

    @Override
    public Object getCredentials() {
        return apiKey;
    }

    @Override
    public Object getDetails() {
        return details; // Return the stored details
    }

    @Override
    public Object getPrincipal() {
        return apiKey;
    }

    @Override
    public boolean isAuthenticated() {
        return authenticated;
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) {
        this.authenticated = isAuthenticated;
    }

    public void setDetails(Object details) {
        this.details = details; // Allow setting of details
    }
}
