package com.bizpilot.business.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class CategoryRowRequest {

//    @NotNull
//    private Integer sortOrder;

    @NotNull
    private Map<String, String> fields;
}