package com.bizpilot.business.model;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BusinessCategoryData {

    private Long id;

    private String fieldKey;

    private String fieldValue;

    private Integer sortOrder;
}