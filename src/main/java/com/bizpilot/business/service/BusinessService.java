package com.bizpilot.business.service;

import com.bizpilot.business.dto.request.BusinessRegistrationRequest;
import com.bizpilot.business.entity.BusinessEntity;
import com.bizpilot.business.mapper.BusinessMapper;
import com.bizpilot.business.model.Business;
import com.bizpilot.business.model.BusinessCategory;
import com.bizpilot.business.repository.BusinessRepository;
import com.bizpilot.common.exception.BusinessNotFoundException;
import com.bizpilot.common.exception.DuplicateSlugException;
import org.springframework.stereotype.Service;

@Service
public class BusinessService {

    private final BusinessRepository businessRepository;
    private final BusinessMapper businessMapper;

    public BusinessService(BusinessRepository businessRepository,
                           BusinessMapper businessMapper) {
        this.businessRepository = businessRepository;
        this.businessMapper = businessMapper;
    }

    public Business register(BusinessRegistrationRequest request) {

        // Convert Request DTO -> Entity
        BusinessEntity entity = businessMapper.toEntity(request);

        // Generate Slug
        String slug = generateSlug(request.getBusinessName());

        // Check Duplicate Slug
        if (businessRepository.existsBySlug(slug)) {
            throw new DuplicateSlugException(slug);
        }

        // Set Generated Values
        entity.setSlug(slug);
        entity.setTheme(getDefaultTheme(request.getCategory()));
        entity.setPublished(false);

        // Save
        entity = businessRepository.save(entity);

        // Entity -> Domain Model
        return businessMapper.toModel(entity);
    }

    private String generateSlug(String businessName) {

        return businessName
                .trim()
                .toLowerCase()
                .replaceAll("[^a-z0-9\\s]", "")
                .replaceAll("\\s+", "-");
    }

    private String getDefaultTheme(BusinessCategory category) {

        return switch (category) {

            case RESTAURANT -> "restaurant-modern";

            case SALON -> "salon-modern";

            case GYM -> "gym-modern";

            case CLINIC -> "clinic-modern";
        };
    }

    public Business findBySlug(String slug) {

        BusinessEntity entity = businessRepository
                .findBySlugAndPublishedTrue(slug)
                .orElseThrow(() ->
                        new BusinessNotFoundException(slug));

        return businessMapper.toModel(entity);
    }


}