package dev.wisespirit.modul9.dto.auth;

import jakarta.validation.constraints.NotBlank;

import java.io.Serializable;

public record AuthRoleCreateDto(@NotBlank(message = "name cannot be blank") String name,
                                @NotBlank(message = "description cannot be blank") String description) implements Serializable {
}
