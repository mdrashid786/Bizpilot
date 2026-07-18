package com.bizpilot.authentication.controller;


import com.bizpilot.authentication.dto.request.LoginRequest;
import com.bizpilot.authentication.dto.request.LogoutRequest;
import com.bizpilot.authentication.dto.request.RefreshTokenRequest;
import com.bizpilot.authentication.dto.request.RegisterRequest;
import com.bizpilot.authentication.dto.response.AuthResponse;
import com.bizpilot.authentication.service.AuthService;
import com.bizpilot.authentication.util.DeviceUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;
    private final DeviceUtils deviceUtils;


    public AuthController(AuthService authService, DeviceUtils deviceUtils) {
        this.authService = authService;
        this.deviceUtils = deviceUtils;
    }

//    @PostMapping("/register")
//    public ResponseEntity<AuthResponse> register(
//            @Valid @RequestBody RegisterRequest request) {
//
//        return ResponseEntity.status(HttpStatus.CREATED)
//                .body(authService.register(request));
//    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(
            @Valid @RequestBody RegisterRequest request,
            @RequestHeader(value = "X-Device-Id", required = false) String deviceId,
            HttpServletRequest httpRequest) {

        String resolvedDeviceId = (deviceId == null || deviceId.isBlank())
                ? UUID.randomUUID().toString()
                : deviceId;

        String deviceName = deviceUtils.extractDeviceName(httpRequest);
        String ipAddress = deviceUtils.extractIp(httpRequest);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(authService.register(request, resolvedDeviceId, deviceName, ipAddress));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(
            @Valid @RequestBody LoginRequest request, HttpServletRequest httpRequest) {

        String ipAddress = deviceUtils.extractIp(httpRequest);
        return ResponseEntity.ok(authService.login(request, ipAddress));

    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody(required = false) LogoutRequest request) {

        authService.logout(authHeader, request);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refresh(
            @RequestParam String refreshToken){
        System.out.println("refreshToken :"+refreshToken);

        return ResponseEntity.ok(
                authService.refresh(refreshToken)
        );

    }

//    @PostMapping("/refresh")
//    public ResponseEntity<AuthResponse> refresh(@Valid @RequestBody RefreshTokenRequest request) {
//        return ResponseEntity.ok(authService.refreshAccessToken(request.getRefreshToken()));
//    }

}
