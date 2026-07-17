package com.bizpilot.authentication.service;


import com.bizpilot.authentication.dto.request.LoginRequest;
import com.bizpilot.authentication.dto.request.LogoutRequest;
import com.bizpilot.authentication.dto.request.RegisterRequest;
import com.bizpilot.authentication.dto.response.AuthResponse;
import com.bizpilot.authentication.entity.RefreshTokenEntity;
import com.bizpilot.authentication.entity.UserEntity;
import com.bizpilot.authentication.jwt.JwtService;
import com.bizpilot.authentication.mapper.UserMapper;
import com.bizpilot.authentication.repository.RefreshTokenRepository;
import com.bizpilot.authentication.repository.UserRepository;
import com.bizpilot.common.exception.EmailAlreadyExistsException;
import com.bizpilot.common.exception.InvalidCredentialsException;
import com.bizpilot.common.exception.InvalidRefreshTokenException;
import com.bizpilot.common.exception.PhoneAlreadyExistsException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
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

    Optional<RefreshTokenEntity> findByToken(String token){
        return refreshTokenRepository.findByToken(token);
    }

    public AuthResponse refreshAccessToken(String refreshTokenValue) {

        RefreshTokenEntity storedToken = refreshTokenRepository.findByToken(refreshTokenValue)
                .orElseThrow(() -> new InvalidRefreshTokenException("Invalid refresh token"));

        if (storedToken.isRevoked()) {
            throw new InvalidRefreshTokenException("Refresh token has been revoked");
        }

        if (storedToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new InvalidRefreshTokenException("Refresh token has expired");
        }

        UserEntity user = storedToken.getUser();

        String newAccessToken = jwtService.generateAccessToken(user);

        return AuthResponse.builder()
                .accessToken(newAccessToken)
                .refreshToken(refreshTokenValue) // same refresh token wapas bhejo, naya generate nahi karna
                .user(userMapper.toResponse(user))
                .build();
    }

//    public AuthResponse register(RegisterRequest request) {
//
//        if (userRepository.existsByEmail(request.getEmail())) {
//            throw new EmailAlreadyExistsException(request.getEmail());
//        }
//
//        if (userRepository.existsByPhone(request.getPhone())) {
//            throw new PhoneAlreadyExistsException(request.getPhone());
//        }
//
//        UserEntity user = userMapper.toEntity(request);
//
//        user.setPassword(passwordEncoder.encode(request.getPassword()));
//
//        user = userRepository.save(user);
//
//        String accessToken = jwtService.generateAccessToken(user);
//
//        String refreshTokenValue = jwtService.generateRefreshToken(user);
//
//        RefreshTokenEntity refreshToken =
//                RefreshTokenEntity.builder()
//                        .token(refreshTokenValue)
//                        .user(user)
//                        .expiryDate(LocalDateTime.now().plusDays(30))
//                        .revoked(false)
//                        .deviceId(request.getDeviceId())
//                        .deviceName(request.getDeviceName())
//                        .ipAddress(ipAddress) // niche dekho isko kaise nikale
//                        .build();
//
//        refreshTokenRepository.save(refreshToken);
//
//        return AuthResponse.builder()
//                .accessToken(accessToken)
//                .refreshToken(refreshTokenValue)
//                .user(userMapper.toResponse(user))
//                .build();
//    }

    public AuthResponse register(RegisterRequest request, String deviceId, String deviceName, String ipAddress) {

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
                        .deviceId(deviceId)
                        .deviceName(deviceName)
                        .ipAddress(ipAddress)
                        .build();

        refreshTokenRepository.save(refreshToken);

        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshTokenValue)
                .user(userMapper.toResponse(user))
                .build();
    }



//    public AuthResponse login(LoginRequest request, String ipAddress) {
//
//        try {
//            authenticationManager.authenticate(
//                    new UsernamePasswordAuthenticationToken(
//                            request.getEmail(),
//                            request.getPassword()
//                    )
//            );
//        } catch (BadCredentialsException ex) {
//            throw new InvalidCredentialsException("Invalid email or password");
//        }
//
//        UserEntity user = userRepository.findByEmail(request.getEmail())
//                .orElseThrow(() -> new InvalidCredentialsException("Invalid email or password"));
//
////        refreshTokenRepository.findByUserAndDeviceId(user, request.getDeviceId())
////                .ifPresent(token -> {
////                    token.setRevoked(true);
////                    refreshTokenRepository.save(token);
////                });
//
//        String accessToken = jwtService.generateAccessToken(user);
//        String refreshTokenValue = jwtService.generateRefreshToken(user);
//
//        RefreshTokenEntity refreshToken = RefreshTokenEntity.builder()
//                .token(refreshTokenValue)
//                .user(user)
//                .deviceId(request.getDeviceId())
//                .deviceName(request.getDeviceName() != null ? request.getDeviceName() : "Unknown Device")
//                .ipAddress(ipAddress) // agla step mein fix karenge
//                .expiryDate(LocalDateTime.now().plusDays(30))
//                .revoked(false)
//                .build();
//
//        refreshTokenRepository.save(refreshToken);
//
//        return AuthResponse.builder()
//                .accessToken(accessToken)
//                .refreshToken(refreshTokenValue)
//                .user(userMapper.toResponse(user))
//                .build();
//    }

public AuthResponse login(LoginRequest request, String ipAddress) {

    try {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );
    } catch (BadCredentialsException ex) {
        throw new InvalidCredentialsException("Invalid email or password");
    }

    UserEntity user = userRepository.findByEmail(request.getEmail())
            .orElseThrow(() -> new InvalidCredentialsException("Invalid email or password"));

    // Same device ke SAARE purane tokens revoke karo (list — crash-proof)
    List<RefreshTokenEntity> existingTokens =
            refreshTokenRepository.findAllByUserAndDeviceId(user, request.getDeviceId());

    existingTokens.forEach(token -> token.setRevoked(true));
    refreshTokenRepository.saveAll(existingTokens);

    String accessToken = jwtService.generateAccessToken(user);
    String refreshTokenValue = jwtService.generateRefreshToken(user);

    RefreshTokenEntity refreshToken = RefreshTokenEntity.builder()
            .token(refreshTokenValue)
            .user(user)
            .deviceId(request.getDeviceId())
            .deviceName(request.getDeviceName() != null ? request.getDeviceName() : "Unknown Device")
            .ipAddress(ipAddress)
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

    public AuthResponse refresh(String refreshToken) {

        RefreshTokenEntity token = refreshTokenRepository
                .findByToken(refreshToken)
                .orElseThrow(() ->
                        new RuntimeException("Invalid refresh token"));

        if (token.isRevoked()) {
            throw new RuntimeException("Refresh token revoked");
        }

        if (!jwtService.isRefreshTokenValid(refreshToken)) {
            throw new RuntimeException("Refresh token expired");
        }

        UserEntity user = token.getUser();

        String accessToken =
                jwtService.generateAccessToken(user);

        String newRefreshToken =
                jwtService.generateRefreshToken(user);

        token.setRevoked(true);

        refreshTokenRepository.save(token);

        RefreshTokenEntity newToken =
                RefreshTokenEntity.builder()

                        .token(newRefreshToken)

                        .user(user)

                        .deviceId(token.getDeviceId())

                        .deviceName(token.getDeviceName())

                        .ipAddress(token.getIpAddress())

                        .expiryDate(LocalDateTime.now().plusDays(30))

                        .revoked(false)

                        .build();

        refreshTokenRepository.save(newToken);

        return AuthResponse.builder()

                .accessToken(accessToken)

                .refreshToken(newRefreshToken)

                .user(userMapper.toResponse(user))

                .build();
    }

    public void logout(String authHeader, LogoutRequest request) {

        String token = authHeader.replace("Bearer ", "");
        String email = jwtService.extractEmail(token);

        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new InvalidCredentialsException("User not found"));

        if (request != null && request.getRefreshToken() != null) {
            // Specific refresh token revoke karo
            refreshTokenRepository.findByToken(request.getRefreshToken())
                    .ifPresent(rt -> {
                        rt.setRevoked(true);
                        refreshTokenRepository.save(rt);
                    });
        }
    }
}