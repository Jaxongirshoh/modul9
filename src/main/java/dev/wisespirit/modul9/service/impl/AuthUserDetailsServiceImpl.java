package dev.wisespirit.modul9.service.impl;


import dev.wisespirit.modul9.dto.auth.CustomUserDetails;
import dev.wisespirit.modul9.entity.AuthUser;
import dev.wisespirit.modul9.repository.AuthUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthUserDetailsServiceImpl implements UserDetailsService {

    private final AuthUserRepository authUserRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AuthUser authUser = authUserRepository.findByUsername(username).orElseThrow(
                () -> new UsernameNotFoundException("bad credentials"));
        return new CustomUserDetails(authUser);
    }



}
