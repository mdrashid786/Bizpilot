package com.bizpilot.business.entity;

import com.bizpilot.authentication.entity.UserEntity;
import com.bizpilot.business.model.BusinessCategory;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "business")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BusinessEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "business_name", nullable = false, length = 150)
    private String businessName;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "owner_id", nullable = false)
    private UserEntity owner;

    @Column(nullable = false, unique = true, length = 120)
    private String slug;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(length = 20)
    private String phone;

    @Column(length = 120)
    private String email;

    @Column(length = 20)
    private String whatsapp;

    @Column(columnDefinition = "TEXT")
    private String address;

    @Column(columnDefinition = "TEXT")
    private String googleMap;

    private String logo;

    private String coverImage;

    @Column(nullable = false)
    private String theme;

    @Column(nullable = false)
    private Boolean published;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BusinessCategory category;


}
