package com.bizpilot.business.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.util.Map;

@Getter
@Builder
public class MenuItemResponse {

    private Integer sortOrder;

    private Map<String, String> values;

}