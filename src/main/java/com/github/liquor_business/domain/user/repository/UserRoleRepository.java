package com.github.liquor_business.domain.user.repository;

import com.github.liquor_business.domain.user.entity.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRoleRepository extends JpaRepository<UserRole, Long> {
}
