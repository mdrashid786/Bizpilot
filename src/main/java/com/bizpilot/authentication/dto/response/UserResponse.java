package com.bizpilot.authentication.dto.response;


import com.bizpilot.authentication.model.UserRole;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserResponse {

    private Long id;

    private String fullName;

    private String email;

    private String phone;

    private UserRole role;

}
