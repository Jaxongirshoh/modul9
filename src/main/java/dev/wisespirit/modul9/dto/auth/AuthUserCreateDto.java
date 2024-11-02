package dev.wisespirit.modul9.dto.auth;

import java.io.Serializable;

public record AuthUserCreateDto(String username, String password, String email) implements Serializable {
}
