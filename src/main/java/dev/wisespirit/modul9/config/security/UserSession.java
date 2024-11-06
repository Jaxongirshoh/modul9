package dev.wisespirit.modul9.config.security;

import dev.wisespirit.modul9.dto.auth.UserSessionData;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class UserSession {

    public UserSessionData requiredUserData(){
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();
        if (authentication==null||authentication instanceof AnonymousAuthenticationToken){
            throw new BadCredentialsException("unauthorized");
        }
        Object principal = authentication.getPrincipal();
        if (principal instanceof UserSessionData uD){
            return uD;
        }
        throw new BadCredentialsException("unauthorized");
    }

    public Long requiredUserId() {
        return requiredUserData().id();
    }
}
