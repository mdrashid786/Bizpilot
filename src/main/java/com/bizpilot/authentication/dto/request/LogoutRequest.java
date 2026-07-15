package com.bizpilot.authentication.dto.request;

import lombok.Data;

@Data
public class LogoutRequest {
    private String refreshToken; // ya deviceId, jo bhejna easy ho
}