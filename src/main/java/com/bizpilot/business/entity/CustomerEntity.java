package com.bizpilot.business.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "customer", uniqueConstraints = @UniqueConstraint(columnNames = {"business_id", "phone"}))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "business_id", nullable = false)
    private BusinessEntity business;

    @Column(nullable = false, length = 120)
    private String name;

    @Column(nullable = false, length = 20)
    private String phone;

    @Column(name = "total_orders", nullable = false)
    @Builder.Default
    private Integer totalOrders = 0;

    @Column(name = "total_spent", nullable = false)
    @Builder.Default
    private Double totalSpent = 0.0;
}