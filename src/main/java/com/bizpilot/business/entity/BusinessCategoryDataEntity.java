package com.bizpilot.business.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "business_category_data")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BusinessCategoryDataEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "business_id")
    private BusinessEntity business;

    @Column(name = "field_key", nullable = false)
    private String fieldKey;

    @Column(name = "row_id", nullable = false, length = 36)
    private String rowId;

    @Column(name = "field_value", columnDefinition = "TEXT")
    private String fieldValue;

    @Column(name = "sort_order")
    private Integer sortOrder;
}