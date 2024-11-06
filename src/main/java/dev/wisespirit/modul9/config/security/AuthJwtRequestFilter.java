package dev.wisespirit.modul9.config.security;

import dev.wisespirit.modul9.dto.auth.UserSessionData;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Deprecated
public class AuthJwtRequestFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;

    public AuthJwtRequestFilter(JwtUtil jwtUtil, UserDetailsService userDetailsService) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }



    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authorization = request.getHeader("Authorization");
        if (authorization==null&&authorization.isBlank()){
            filterChain.doFilter(request, response);
            return;
        }
        String token = authorization.substring(7);
        if (jwtUtil.isValid(token)){
            filterChain.doFilter(request, response);
            return;
        }
        Claims claims = jwtUtil.getClaims(token);
        if (claims.get(token)==null||claims.get(token)=="REFRESH"){
            filterChain.doFilter(request, response);
            return;
        }
        String username = jwtUtil.getUsername(token);
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        UserSessionData userSessionData = new UserSessionData(
                userDetails.getUsername(),
                userDetails.getAuthorities()
        );
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userSessionData, null, userDetails.getAuthorities());
        WebAuthenticationDetails webAuthenticationDetails = new WebAuthenticationDetailsSource().buildDetails(request);
        authentication.setDetails(webAuthenticationDetails);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        filterChain.doFilter(request, response);

    }
}