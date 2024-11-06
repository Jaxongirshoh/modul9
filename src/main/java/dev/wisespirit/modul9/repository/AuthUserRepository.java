package dev.wisespirit.modul9.repository;

import dev.wisespirit.modul9.entity.AuthUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AuthUserRepository extends JpaRepository<AuthUser,Long> {
    Optional<AuthUser> findByUsername(String username);
}
