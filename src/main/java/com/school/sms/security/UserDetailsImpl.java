package com.school.sms.security;

import com.school.sms.auth.entity.User;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

/**
 * Custom UserDetails implementation
 * Wraps User entity and provides Spring Security UserDetails interface
 */
@Getter
public class UserDetailsImpl implements UserDetails {

    private final User user;

    public UserDetailsImpl(User user) {
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(
                new SimpleGrantedAuthority(user.getRole().getAuthority()));
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // Can be customized based on User entity fields
    }

    @Override
    public boolean isAccountNonLocked() {
        return !user.isAccountLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return !user.getPasswordExpired();
    }

    @Override
    public boolean isEnabled() {
        return user.getActive() && user.canLogin();
    }

    /**
     * Get user ID
     */
    public Long getId() {
        return user.getId();
    }

    /**
     * Get email
     */
    public String getEmail() {
        return user.getEmail();
    }
}
