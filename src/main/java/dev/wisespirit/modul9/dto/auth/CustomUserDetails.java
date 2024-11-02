package dev.wisespirit.modul9.dto.auth;

import dev.wisespirit.modul9.entity.AuthPermission;
import dev.wisespirit.modul9.entity.AuthRole;
import dev.wisespirit.modul9.entity.AuthUser;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class CustomUserDetails implements UserDetails {
    private final transient AuthUser authUser;

    public CustomUserDetails(AuthUser authUser) {
        this.authUser = authUser;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<AuthRole> roles = authUser.getRoles();
        if (roles==null){
            return Collections.emptyList();
        }
        List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
        for (AuthRole role : roles){
            authorities.add(new SimpleGrantedAuthority("ROLE_"+role.getName()));
            List<AuthPermission> permissions = role.getPermissions();
            if (permissions!=null){
                for (AuthPermission permission : permissions) {
                    authorities.add(new SimpleGrantedAuthority(permission.getName()));
                }
            }
        }
        return authorities;
    }

    @Override
    public String getPassword() {
        return authUser.getPassword();
    }

    @Override
    public String getUsername() {
        return authUser.getUsername();
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
        return authUser.isActive();
    }
}
