package dev.wisespirit.modul9.dto.auth;

import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public record UserSessionData(Long id,String username, String email,
                              Collection<? extends GrantedAuthority> authorities) {
}
