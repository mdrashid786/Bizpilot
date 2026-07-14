package com.bizpilot.business.dto.response;

import com.bizpilot.business.model.BusinessCategory;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class BusinessResponse {

    private Long id;

    private String businessName;

    private String slug;

    private BusinessCategory category;

    private String theme;

    private Boolean published;
}
