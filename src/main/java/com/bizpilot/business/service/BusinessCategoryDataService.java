package com.bizpilot.business.service;

import com.bizpilot.authentication.entity.UserEntity;
import com.bizpilot.authentication.repository.UserRepository;
import com.bizpilot.business.dto.request.CategoryDataRequest;
import com.bizpilot.business.dto.request.CategoryRowRequest;
import com.bizpilot.business.dto.response.CategoryDataResponse;
import com.bizpilot.business.dto.response.CategoryRowResponse;
import com.bizpilot.business.entity.BusinessCategoryDataEntity;
import com.bizpilot.business.entity.BusinessEntity;
import com.bizpilot.business.mapper.BusinessCategoryDataMapper;
import com.bizpilot.business.repository.BusinessCategoryDataRepository;
import com.bizpilot.business.repository.BusinessRepository;
import com.bizpilot.common.exception.InvalidCategoryFieldException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class BusinessCategoryDataService {

    private final BusinessRepository businessRepository;
    private final BusinessCategoryDataRepository repository;
    private final BusinessCategoryDataMapper mapper;
    private final UserRepository userRepository;
    private final CategoryConfigService categoryConfigService;

    public BusinessCategoryDataService(BusinessRepository businessRepository, BusinessCategoryDataRepository repository,
                                       BusinessCategoryDataMapper mapper, UserRepository userRepository,
                                       CategoryConfigService categoryConfigService) {
        this.businessRepository = businessRepository;
        this.repository = repository;
        this.mapper = mapper;
        this.userRepository = userRepository;
        this.categoryConfigService = categoryConfigService;
    }

    private BusinessEntity getCurrentBusiness() {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        UserEntity owner =
                userRepository.findByEmail(email)
                        .orElseThrow();

        return businessRepository.findByOwner(owner)
                .orElseThrow();
    }

    private void validateField(BusinessEntity business,
                               String fieldKey) {

        boolean valid = categoryConfigService
                .isValidField(
                        business.getCategory(),
                        fieldKey);

        if (!valid) {

            throw new InvalidCategoryFieldException(fieldKey);
        }
    }

    public CategoryDataResponse save(CategoryDataRequest request) {

        BusinessEntity business = getCurrentBusiness();

        BusinessCategoryDataEntity entity = mapper.toEntity(request);

        validateField(business, request.getFieldKey());

        entity.setBusiness(business);

        repository.save(entity);

        return mapper.toResponse(entity);
    }

    public List<CategoryDataResponse> findAll() {

        BusinessEntity business = getCurrentBusiness();

        return repository.findByBusinessIdOrderBySortOrder(business.getId())
                .stream()
                .map(mapper::toResponse)
                .toList();
    }

    public CategoryDataResponse update(Long id,
                                       CategoryDataRequest request) {

        BusinessEntity business = getCurrentBusiness();

        BusinessCategoryDataEntity entity =
                repository.findById(id)
                        .orElseThrow();

        if (!entity.getBusiness().getId().equals(business.getId())) {

            throw new RuntimeException("Access Denied");
        }
        validateField(business, request.getFieldKey());

        entity.setFieldKey(request.getFieldKey());
        entity.setFieldValue(request.getFieldValue());
        entity.setSortOrder(request.getSortOrder());

        repository.save(entity);

        return mapper.toResponse(entity);
    }

    public void delete(Long id) {

        BusinessEntity business = getCurrentBusiness();

        BusinessCategoryDataEntity entity =
                repository.findById(id)
                        .orElseThrow();

        if (!entity.getBusiness().getId().equals(business.getId())) {

            throw new RuntimeException("Access Denied");
        }

        repository.delete(entity);
    }

    public CategoryRowResponse saveRow(CategoryRowRequest request) {

        BusinessEntity business = getCurrentBusiness();

        Integer sortOrder = getNextSortOrder(business);

        for (Map.Entry<String, String> field : request.getFields().entrySet()) {

            validateField(business, field.getKey());

            BusinessCategoryDataEntity entity =
                    BusinessCategoryDataEntity.builder()
                            .business(business)
                            .fieldKey(field.getKey())
                            .fieldValue(field.getValue())
                            .sortOrder(sortOrder)
                            .build();

            repository.save(entity);
        }

        return CategoryRowResponse.builder()
                .sortOrder(sortOrder)
                .fields(request.getFields())
                .build();
    }

    public List<CategoryRowResponse> findRows() {

        BusinessEntity business = getCurrentBusiness();

        List<BusinessCategoryDataEntity> rows =
                repository.findByBusinessIdOrderBySortOrder(business.getId());

        Map<Integer, Map<String, String>> grouped =
                new LinkedHashMap<>();

        for (BusinessCategoryDataEntity row : rows) {

            grouped.computeIfAbsent(
                    row.getSortOrder(),
                    k -> new LinkedHashMap<>());

            grouped.get(row.getSortOrder())
                    .put(row.getFieldKey(), row.getFieldValue());
        }

        return grouped.entrySet()
                .stream()
                .map(entry ->
                        CategoryRowResponse.builder()
                                .sortOrder(entry.getKey())
                                .fields(entry.getValue())
                                .build())
                .toList();
    }



    private Integer getNextSortOrder(BusinessEntity business) {

        return repository.findMaxSortOrder(business.getId()) + 1;
    }

    public CategoryRowResponse updateRow(
            Integer sortOrder,
            CategoryRowRequest request) {

        BusinessEntity business = getCurrentBusiness();

        List<BusinessCategoryDataEntity> rows =
                repository.findByBusinessIdAndSortOrder(
                        business.getId(),
                        sortOrder);

        if (rows.isEmpty()) {
            throw new RuntimeException("Row not found");
        }

        // Validate all incoming fields
        for (String fieldKey : request.getFields().keySet()) {
            validateField(business, fieldKey);
        }

        // Existing data ko Map me convert karo
        Map<String, BusinessCategoryDataEntity> existing =
                rows.stream()
                        .collect(Collectors.toMap(
                                BusinessCategoryDataEntity::getFieldKey,
                                e -> e
                        ));

        // Update existing fields
        for (Map.Entry<String, String> entry : request.getFields().entrySet()) {

            BusinessCategoryDataEntity entity =
                    existing.get(entry.getKey());

            if (entity != null) {

                entity.setFieldValue(entry.getValue());

                repository.save(entity);

            }
        }

        return CategoryRowResponse.builder()
                .sortOrder(sortOrder)
                .fields(request.getFields())
                .build();
    }


}