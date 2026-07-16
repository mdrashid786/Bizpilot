package com.bizpilot.authentication.service;


import com.bizpilot.authentication.dto.request.ChangePasswordRequest;
import com.bizpilot.authentication.dto.request.UpdateProfileRequest;
import com.bizpilot.authentication.dto.response.UserResponse;
import com.bizpilot.authentication.entity.UserEntity;
import com.bizpilot.authentication.repository.UserRepository;
import com.bizpilot.authentication.service.UserService;
import com.bizpilot.common.exception.InvalidPasswordException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserResponse getCurrentUser() {
        UserEntity user = getLoggedInUser();
        return toResponse(user);
    }

    public UserResponse updateProfile(UpdateProfileRequest request) {

        UserEntity user = getLoggedInUser();

        user.setFullName(request.getFullName());
        user.setPhone(request.getPhone());

        user = userRepository.save(user);

        return toResponse(user);
    }

    public void changePassword(ChangePasswordRequest request) {

        UserEntity user = getLoggedInUser();

        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new InvalidPasswordException("Current password is incorrect");
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
    }

    private UserEntity getLoggedInUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    private UserResponse toResponse(UserEntity user) {
        return UserResponse.builder()
                .id(user.getId())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .phone(user.getPhone())
                .role(user.getRole())
                .build();
    }
}
