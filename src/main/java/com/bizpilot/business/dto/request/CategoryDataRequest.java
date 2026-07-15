package com.bizpilot.business.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class CategoryDataRequest {

//    private Map<String, String> values;

    @NotBlank
    private String fieldKey;

    @NotBlank
    private String fieldValue;

    private Integer sortOrder;
}
