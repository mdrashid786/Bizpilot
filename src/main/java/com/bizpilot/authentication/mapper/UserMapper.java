package com.bizpilot.authentication.mapper;

import com.bizpilot.authentication.dto.request.RegisterRequest;
import com.bizpilot.authentication.dto.response.UserResponse;
import com.bizpilot.authentication.entity.UserEntity;
import com.bizpilot.authentication.model.UserRole;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserEntity toEntity(RegisterRequest request) {

        if (request == null) {
            return null;
        }

        return UserEntity.builder()
                .fullName(request.getFullName())
                .email(request.getEmail())
                .phone(request.getPhone())
                .password(request.getPassword())
                .role(UserRole.OWNER)
                .enabled(true)
                .build();
    }

    public UserResponse toResponse(UserEntity entity) {

        if (entity == null) {
            return null;
        }

        return UserResponse.builder()
                .id(entity.getId())
                .fullName(entity.getFullName())
                .email(entity.getEmail())
                .phone(entity.getPhone())
                .role(entity.getRole())
                .build();
    }
}