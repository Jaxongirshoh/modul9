package dev.wisespirit.modul9.dto.auth;

import jakarta.validation.constraints.NotBlank;

public record RefreshTokenRequest(@NotBlank(message = "token cannot be blank") String token) {
}
