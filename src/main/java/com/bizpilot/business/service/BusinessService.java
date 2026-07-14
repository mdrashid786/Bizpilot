package com.bizpilot.business.service;

import com.bizpilot.authentication.entity.UserEntity;
import com.bizpilot.authentication.repository.UserRepository;
import com.bizpilot.business.dto.request.BusinessRegistrationRequest;
import com.bizpilot.business.entity.BusinessEntity;
import com.bizpilot.business.mapper.BusinessMapper;
import com.bizpilot.business.model.Business;
import com.bizpilot.business.model.CategoryConfig;
import com.bizpilot.business.repository.BusinessRepository;
import com.bizpilot.common.exception.BusinessNotFoundException;
import com.bizpilot.common.exception.DuplicateSlugException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class BusinessService {

    private final BusinessRepository businessRepository;
    private final BusinessMapper businessMapper;
    private final UserRepository userRepository;
    private final CategoryConfigService categoryConfigService;

    public BusinessService(BusinessRepository businessRepository,
                           BusinessMapper businessMapper,
                           UserRepository userRepository,
                           CategoryConfigService categoryConfigService) {

        this.businessRepository = businessRepository;
        this.businessMapper = businessMapper;
        this.userRepository = userRepository;
        this.categoryConfigService = categoryConfigService;
    }

    public Business register(BusinessRegistrationRequest request) {

        // 1. Create Business Model
        Business business = Business.builder()
                .businessName(request.getBusinessName())
                .phone(request.getPhone())
                .email(request.getEmail())
                .whatsapp(request.getWhatsapp())
                .address(request.getAddress())
                .description(request.getDescription())
                .category(request.getCategory())
                .build();

        // 2. Generate Slug
        String slug = generateSlug(business.getBusinessName());

        if (businessRepository.existsBySlug(slug)) {
            throw new DuplicateSlugException(slug);
        }

        business.setSlug(slug);

        // 3. Load Category Config
        CategoryConfig config =
                categoryConfigService.load(business.getCategory());

        business.setTheme(config.getTemplate());

        business.setPublished(false);

        // 4. Logged In User
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        UserEntity owner = userRepository
                .findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // 5. Convert Model -> Entity
        BusinessEntity entity = businessMapper.toEntity(business);

        entity.setOwner(owner);

        // 6. Save
        entity = businessRepository.save(entity);

        // 7. Entity -> Model
        return businessMapper.toModel(entity);
    }

    public Business findBySlug(String slug) {

        BusinessEntity entity = businessRepository
                .findBySlugAndPublishedTrue(slug)
                .orElseThrow(() ->
                        new BusinessNotFoundException(slug));

        return businessMapper.toModel(entity);
    }

    private String generateSlug(String businessName) {

        return businessName
                .trim()
                .toLowerCase()
                .replaceAll("[^a-z0-9\\s-]", "")
                .replaceAll("\\s+", "-")
                .replaceAll("-+", "-");
    }

    Optional<BusinessEntity> findByOwner(UserEntity owner){
       return businessRepository.findByOwner(owner);
    }

    public Business getMyBusiness() {

        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        String email = authentication.getName();

        UserEntity owner = userRepository
                .findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        BusinessEntity entity =
                findByOwner(owner)
                .orElseThrow(() ->
                        new RuntimeException("Business not found"));

        return businessMapper.toModel(entity);
    }
}