package com.bizpilot.business.dto.response;

import com.bizpilot.business.model.BusinessCategory;
import com.bizpilot.business.model.CategoryField;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class CategoryConfigResponse {
    private BusinessCategory category;
    private String dashboardSectionLabel;
    private List<CategoryField> fields;
}