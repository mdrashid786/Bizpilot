package com.bizpilot.authentication.repository;

import com.bizpilot.authentication.entity.RefreshTokenEntity;
import com.bizpilot.authentication.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshTokenEntity, Long> {

    Optional<RefreshTokenEntity> findByToken(String token);

    Optional<RefreshTokenEntity> findByUser(UserEntity user);

    void deleteByUser(UserEntity user);

}