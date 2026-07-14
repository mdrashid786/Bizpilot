package com.bizpilot.business.mapper;

import com.bizpilot.business.dto.request.BusinessRegistrationRequest;
import com.bizpilot.business.dto.response.BusinessResponse;
import com.bizpilot.business.entity.BusinessEntity;
import com.bizpilot.business.model.Business;
import org.springframework.stereotype.Component;

@Component
public class BusinessMapper {

    public Business toModel(BusinessEntity entity) {

        if (entity == null) {
            return null;
        }

        return Business.builder()
                .id(entity.getId())
                .businessName(entity.getBusinessName())
                .slug(entity.getSlug())
                .description(entity.getDescription())
                .phone(entity.getPhone())
                .email(entity.getEmail())
                .whatsapp(entity.getWhatsapp())
                .address(entity.getAddress())
                .googleMap(entity.getGoogleMap())
                .logo(entity.getLogo())
                .coverImage(entity.getCoverImage())
                .theme(entity.getTheme())
                .published(entity.getPublished())
                .category(entity.getCategory())
                .build();
    }

    public BusinessEntity toEntity(Business model) {

        if (model == null) {
            return null;
        }

        return BusinessEntity.builder()
                .id(model.getId())
                .businessName(model.getBusinessName())
                .slug(model.getSlug())
                .description(model.getDescription())
                .phone(model.getPhone())
                .email(model.getEmail())
                .whatsapp(model.getWhatsapp())
                .address(model.getAddress())
                .googleMap(model.getGoogleMap())
                .logo(model.getLogo())
                .coverImage(model.getCoverImage())
                .theme(model.getTheme())
                .published(model.getPublished())
                .category(model.getCategory())
                .build();
    }

    public BusinessResponse toResponse(Business business){

        return BusinessResponse.builder()
                .id(business.getId())
                .businessName(business.getBusinessName())
                .slug(business.getSlug())
                .category(business.getCategory())
                .theme(business.getTheme())
                .published(business.getPublished())
                .build();
    }

    public BusinessEntity toEntity(BusinessRegistrationRequest request) {

        if (request == null) {
            return null;
        }

        return BusinessEntity.builder()
                .businessName(request.getBusinessName())
                .phone(request.getPhone())
                .email(request.getEmail())
                .whatsapp(request.getWhatsapp())
                .address(request.getAddress())
                .description(request.getDescription())
                .category(request.getCategory())
                .build();
    }
}
