package com.bizpilot.authentication.service;


import com.bizpilot.authentication.dto.request.LoginRequest;
import com.bizpilot.authentication.dto.request.RegisterRequest;
import com.bizpilot.authentication.dto.response.AuthResponse;
import com.bizpilot.authentication.entity.RefreshTokenEntity;
import com.bizpilot.authentication.entity.UserEntity;
import com.bizpilot.authentication.jwt.JwtService;
import com.bizpilot.authentication.mapper.UserMapper;
import com.bizpilot.authentication.repository.RefreshTokenRepository;
import com.bizpilot.authentication.repository.UserRepository;
import com.bizpilot.common.exception.EmailAlreadyExistsException;
import com.bizpilot.common.exception.PhoneAlreadyExistsException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
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
    private final AuthenticationManager authenticationManager;

    public AuthService(UserRepository userRepository,
                       RefreshTokenRepository refreshTokenRepository,
                       UserMapper userMapper,
                       PasswordEncoder passwordEncoder,
                       JwtService jwtService,
                       AuthenticationManager authenticationManager) {

        this.userRepository = userRepository;
        this.refreshTokenRepository = refreshTokenRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    public AuthResponse register(RegisterRequest request) {

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new EmailAlreadyExistsException(request.getEmail());
        }

        if (userRepository.existsByPhone(request.getPhone())) {
            throw new PhoneAlreadyExistsException(request.getPhone());
        }

        UserEntity user = userMapper.toEntity(request);

        user.setPassword(passwordEncoder.encode(request.getPassword()));

        user = userRepository.save(user);

        String accessToken = jwtService.generateAccessToken(user);

        String refreshTokenValue = jwtService.generateRefreshToken(user);

        RefreshTokenEntity refreshToken =
                RefreshTokenEntity.builder()
                        .token(refreshTokenValue)
                        .user(user)
                        .expiryDate(LocalDateTime.now().plusDays(30))
                        .revoked(false)
                        .build();

        refreshTokenRepository.save(refreshToken);

        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshTokenValue)
                .user(userMapper.toResponse(user))
                .build();
    }


    public AuthResponse login(LoginRequest request) {

        authenticationManager.authenticate(

                new UsernamePasswordAuthenticationToken(

                        request.getEmail(),

                        request.getPassword()

                )

        );

        UserEntity user = userRepository.findByEmail(request.getEmail())

                .orElseThrow();

        String accessToken = jwtService.generateAccessToken(user);

        String refreshTokenValue = jwtService.generateRefreshToken(user);

        RefreshTokenEntity refreshToken = RefreshTokenEntity.builder()

                .token(refreshTokenValue)

                .user(user)

                .expiryDate(LocalDateTime.now().plusDays(30))

                .revoked(false)

                .build();

        refreshTokenRepository.save(refreshToken);

        return AuthResponse.builder()

                .accessToken(accessToken)

                .refreshToken(refreshTokenValue)

                .user(userMapper.toResponse(user))

                .build();
    }

}