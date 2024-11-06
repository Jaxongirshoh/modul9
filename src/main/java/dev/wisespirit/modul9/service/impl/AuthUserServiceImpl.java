package dev.wisespirit.modul9.service.impl;

import dev.wisespirit.modul9.config.security.JwtUtil;
import dev.wisespirit.modul9.config.security.UserSession;
import dev.wisespirit.modul9.dto.auth.AuthUserCreateDto;
import dev.wisespirit.modul9.dto.auth.GenerateTokenRequest;
import dev.wisespirit.modul9.dto.auth.RefreshTokenRequest;
import dev.wisespirit.modul9.dto.auth.TokenResponse;
import dev.wisespirit.modul9.dto.auth.UserSessionData;
import dev.wisespirit.modul9.entity.AuthUser;
import dev.wisespirit.modul9.enums.JwtTokenType;
import dev.wisespirit.modul9.repository.AuthUserRepository;
import dev.wisespirit.modul9.service.AuthUserService;
import io.jsonwebtoken.Claims;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class AuthUserServiceImpl implements AuthUserService {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder bycryptPasswordEncoder;
    private final AuthUserRepository authUserRepository;
    private final UserSession userSession;
    public AuthUserServiceImpl(AuthenticationManager authenticationManager, JwtUtil jwtUtil, PasswordEncoder passwordEncoder, PasswordEncoder bycryptPasswordEncoder, AuthUserRepository authUserRepository, UserSession userSession) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.bycryptPasswordEncoder = bycryptPasswordEncoder;
        this.authUserRepository = authUserRepository;
        this.userSession = userSession;
    }

    @Override
    public TokenResponse generateAccessToken(GenerateTokenRequest dto) {
        String username = dto.username();
        String password = dto.password();
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                new UsernamePasswordAuthenticationToken(username, password);
        authenticationManager.authenticate(usernamePasswordAuthenticationToken);
        Map<String, Object> accessToken = Map.<String, Object>of("token", JwtTokenType.ACCESS);
        Map<String, Object> refreshToken = Map.<String, Object>of("token", JwtTokenType.REFRESH);
        String generateAccessTokenClaims = jwtUtil.generateAccessToken(username, accessToken);
        String generateRefreshTokenClaims = jwtUtil.generateRefreshToken(username, refreshToken);
        return new TokenResponse(generateAccessTokenClaims, generateRefreshTokenClaims);
    }

    @Override
    public Long userCreate(AuthUserCreateDto dto) {
        AuthUser authUser = new AuthUser();
        authUser.setUsername(dto.username());
        authUser.setPassword(bycryptPasswordEncoder.encode(dto.password()));
        authUser.setEmail(dto.email());
        AuthUser saved = authUserRepository.save(authUser);
        return saved.getId();
    }

    @Override
    public TokenResponse refreshToken(RefreshTokenRequest dto) {
        String refreshToken = dto.token();
        if (!jwtUtil.isValid(refreshToken)) {
            throw new BadCredentialsException("Invalid refresh token");
        }
        Claims claims = jwtUtil.getClaims(refreshToken);
        if (claims.get("token")==null||claims.get("teoken").equals("REFRESH")){
            throw new BadCredentialsException("Invalid refresh token");
        }
        Map<String, Object> accessTokenClaims = Map.<String, Object>of("refreshToken", JwtTokenType.REFRESH);
        String usernam = claims.get("sub", String.class);
        String accessToken = jwtUtil.generateAccessToken(usernam, accessTokenClaims);
        return new TokenResponse(accessToken,refreshToken);
    }

    @Override
    public UserSessionData getMe() {
        return userSession.requiredUserData();
    }
}
