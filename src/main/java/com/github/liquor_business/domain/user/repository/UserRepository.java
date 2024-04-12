package com.github.liquor_business.domain.user.repository;

import com.github.liquor_business.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Boolean existsByEmail(String email);
    Boolean existsByName(String name);

    Optional<User> findByEmail(String email);

}
