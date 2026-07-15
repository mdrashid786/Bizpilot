package com.bizpilot.business.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CategoryDataResponse {

    private Long id;

    private String fieldKey;

    private String fieldValue;

    private Integer sortOrder;
}