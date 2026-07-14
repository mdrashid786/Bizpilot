package com.bizpilot.authentication.service;


import com.bizpilot.authentication.dto.request.RegisterRequest;
import com.bizpilot.authentication.dto.response.AuthResponse;
import com.bizpilot.authentication.entity.UserEntity;
import com.bizpilot.authentication.mapper.UserMapper;
import com.bizpilot.authentication.repository.RefreshTokenRepository;
import com.bizpilot.authentication.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(UserRepository userRepository,
                       RefreshTokenRepository refreshTokenRepository,
                       UserMapper userMapper,
                       PasswordEncoder passwordEncoder,
                       JwtService jwtService) {

        this.userRepository = userRepository;
        this.refreshTokenRepository = refreshTokenRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public AuthResponse register(RegisterRequest request) {

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already registered.");
        }

        if (userRepository.existsByPhone(request.getPhone())) {
            throw new RuntimeException("Phone already registered.");
        }

        UserEntity user = userMapper.toEntity(request);

        user.setPassword(passwordEncoder.encode(request.getPassword()));

        user = userRepository.save(user);

        String accessToken = jwtService.generateAccessToken(user);

        RefreshTokenEntity refreshToken = RefreshTokenEntity.builder()
                .token(UUID.randomUUID().toString())
                .user(user)
                .expiryDate(LocalDateTime.now().plusDays(30))
                .build();

        refreshTokenRepository.save(refreshToken);

        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken.getToken())
                .user(userMapper.toResponse(user))
                .build();
    }

}