package com.bizpilot.business.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BusinessUpdateRequest {

    @NotBlank
    private String businessName;

    @NotBlank
    private String phone;

    @Email
    private String email;

    private String whatsapp;

    private String address;

    private String description;
    private String googleMap;   // 👈 naya field
    private String tagline;
    private String businessHours;
    private String instagramUrl;
    private String facebookUrl;

    // category jaan-bujh kar nahi hai — update mein change nahi hona chahiye
}