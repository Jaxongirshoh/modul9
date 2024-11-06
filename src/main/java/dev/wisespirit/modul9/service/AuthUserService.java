package dev.wisespirit.modul9.service;

import dev.wisespirit.modul9.dto.auth.AuthUserCreateDto;
import dev.wisespirit.modul9.dto.auth.GenerateTokenRequest;
import dev.wisespirit.modul9.dto.auth.RefreshTokenRequest;
import dev.wisespirit.modul9.dto.auth.TokenResponse;
import dev.wisespirit.modul9.dto.auth.UserSessionData;

public interface AuthUserService {
    TokenResponse generateAccessToken(GenerateTokenRequest dto);
    Long userCreate(AuthUserCreateDto dto);
    TokenResponse refreshToken(RefreshTokenRequest dto);
    UserSessionData getMe();

}
