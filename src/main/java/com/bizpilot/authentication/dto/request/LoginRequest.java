package com.bizpilot.authentication.dto.request;


import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequest {

    @NotBlank
    private String email;

    @NotBlank
    private String password;

    private String deviceId;

    private String deviceName;

    private String ipAddress;


}
