package com.hhplus.precourse.user.repository;

import com.hhplus.precourse.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByName(String name);

    boolean existsByName(String name);
}
