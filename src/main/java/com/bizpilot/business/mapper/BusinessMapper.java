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
                .tagline(entity.getTagline())
                .businessHours(entity.getBusinessHours())
                .instagramUrl(entity.getInstagramUrl())
                .facebookUrl(entity.getFacebookUrl())
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
                .published(model.getPublished())
                .theme(model.getTheme())
                .category(model.getCategory())
                .tagline(model.getTagline())
                .businessHours(model.getBusinessHours())
                .instagramUrl(model.getInstagramUrl())
                .facebookUrl(model.getFacebookUrl())
                .build();
    }
    public BusinessResponse toResponse(Business business){

        return BusinessResponse.builder()
                .id(business.getId())
                .businessName(business.getBusinessName())
                .slug(business.getSlug())
                .description(business.getDescription())
                .phone(business.getPhone())
                .email(business.getEmail())
                .whatsapp(business.getWhatsapp())
                .address(business.getAddress())
                .googleMap(business.getGoogleMap())
                .logo(business.getLogo())
                .coverImage(business.getCoverImage())
                .category(business.getCategory())
                .theme(business.getTheme())
                .published(business.getPublished())
                .tagline(business.getTagline())
                .businessHours(business.getBusinessHours())
                .instagramUrl(business.getInstagramUrl())
                .facebookUrl(business.getFacebookUrl())
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
                .googleMap(request.getGoogleMap())        // 👈 naya
                .tagline(request.getTagline())              // 👈 naya
                .businessHours(request.getBusinessHours())  // 👈 naya
                .instagramUrl(request.getInstagramUrl())    // 👈 naya
                .facebookUrl(request.getFacebookUrl())      // 👈 naya
                .build();
    }

}
