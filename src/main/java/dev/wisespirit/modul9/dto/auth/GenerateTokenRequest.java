package dev.wisespirit.modul9.dto.auth;

import jakarta.validation.constraints.NotBlank;

public record GenerateTokenRequest(@NotBlank(message = "username cannot be blank") String username,
                                   @NotBlank(message = "password cannot be blank") String password) {
}
