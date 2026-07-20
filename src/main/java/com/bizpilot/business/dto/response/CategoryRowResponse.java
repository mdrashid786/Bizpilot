package com.bizpilot.business.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.util.Map;

@Getter
@Builder
public class CategoryRowResponse {
    private String rowId;
    private Integer sortOrder;
    private Map<String, String> fields;
    private Boolean active;
    private Boolean featured;


}