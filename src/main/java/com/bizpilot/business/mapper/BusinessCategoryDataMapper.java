package com.bizpilot.business.mapper;

import com.bizpilot.business.dto.request.CategoryDataRequest;
import com.bizpilot.business.dto.response.CategoryDataResponse;
import com.bizpilot.business.entity.BusinessCategoryDataEntity;
import com.bizpilot.business.model.BusinessCategoryData;
import org.springframework.stereotype.Component;

@Component
public class BusinessCategoryDataMapper {

    public BusinessCategoryData toModel(
            BusinessCategoryDataEntity entity) {

        if (entity == null)
            return null;

        return BusinessCategoryData.builder()
                .id(entity.getId())
                .fieldKey(entity.getFieldKey())
                .fieldValue(entity.getFieldValue())
                .sortOrder(entity.getSortOrder())
                .build();
    }

    public BusinessCategoryDataEntity toEntity(
            BusinessCategoryData model) {

        if (model == null)
            return null;

        return BusinessCategoryDataEntity.builder()
                .id(model.getId())
                .fieldKey(model.getFieldKey())
                .fieldValue(model.getFieldValue())
                .sortOrder(model.getSortOrder())
                .build();
    }

    public BusinessCategoryDataEntity toEntity(CategoryDataRequest request) {

        return BusinessCategoryDataEntity.builder()
                .fieldKey(request.getFieldKey())
                .fieldValue(request.getFieldValue())
                .sortOrder(request.getSortOrder())
                .build();
    }

    public CategoryDataResponse toResponse(BusinessCategoryDataEntity entity) {

        return CategoryDataResponse.builder()
                .id(entity.getId())
                .fieldKey(entity.getFieldKey())
                .fieldValue(entity.getFieldValue())
                .sortOrder(entity.getSortOrder())
                .build();
    }
}