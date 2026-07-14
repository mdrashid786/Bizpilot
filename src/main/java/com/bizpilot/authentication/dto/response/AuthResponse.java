package com.bizpilot.authentication.dto.response;


import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AuthResponse {

    private String accessToken;

    private String refreshToken;

    private UserResponse user;

}