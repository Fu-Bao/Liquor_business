package com.github.liquor_business.domain.user.repository;

import com.github.liquor_business.domain.user.entitiy.SocialType;
import com.github.liquor_business.domain.user.entitiy.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByEmail(String email);

    Optional<UserEntity> findByUsername(String username);

    Optional<UserEntity> findByRefreshToken(String refreshToken);

    Optional<UserEntity> findBySocialTypeAndSocialId(SocialType socialType, String socialId);
}
