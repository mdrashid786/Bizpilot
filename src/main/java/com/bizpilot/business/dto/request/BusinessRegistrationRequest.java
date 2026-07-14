package com.bizpilot.business.dto.request;

import com.bizpilot.business.model.BusinessCategory;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BusinessRegistrationRequest {

    @NotBlank
    private String businessName;

    @NotBlank
    private String phone;

    @Email
    private String email;

    private String whatsapp;

    private String address;

    private String description;

    private BusinessCategory category;
}
