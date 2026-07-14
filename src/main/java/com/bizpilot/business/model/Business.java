package com.bizpilot.business.model;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

/**
 * Represents a business profile displayed on the public website.
 */

public class Business {

    private Long id;

    private String businessName;

    private String slug;

    private String description;

    private String phone;

    private String email;

    private String whatsapp;

    private String address;

    private String googleMap;

    private String logo;

    private String coverImage;

    private String theme;

    private boolean published;

}
