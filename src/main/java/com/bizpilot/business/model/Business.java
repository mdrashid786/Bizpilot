package com.bizpilot.business.model;

import lombok.*;

/**
 * Represents a business profile displayed on the public website.
 */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
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

    private BusinessCategory category;

}
