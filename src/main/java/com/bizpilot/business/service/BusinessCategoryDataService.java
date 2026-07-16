package com.bizpilot.business.service;

import com.bizpilot.authentication.entity.UserEntity;
import com.bizpilot.authentication.repository.UserRepository;
import com.bizpilot.business.dto.request.CategoryDataRequest;
import com.bizpilot.business.dto.request.CategoryRowRequest;
import com.bizpilot.business.dto.response.CategoryConfigResponse;
import com.bizpilot.business.dto.response.CategoryDataResponse;
import com.bizpilot.business.dto.response.CategoryRowResponse;
import com.bizpilot.business.entity.BusinessCategoryDataEntity;
import com.bizpilot.business.entity.BusinessEntity;
import com.bizpilot.business.mapper.BusinessCategoryDataMapper;
import com.bizpilot.business.model.CategoryConfig;
import com.bizpilot.business.model.CategoryField;
import com.bizpilot.business.repository.BusinessCategoryDataRepository;
import com.bizpilot.business.repository.BusinessRepository;
import com.bizpilot.common.exception.InvalidCategoryFieldException;
import com.bizpilot.common.exception.InvalidFieldException;
import com.bizpilot.common.exception.RowNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class BusinessCategoryDataService {

    private final BusinessRepository businessRepository;
    private final BusinessCategoryDataRepository repository;
    private final BusinessCategoryDataMapper mapper;
    private final UserRepository userRepository;
    private final CategoryConfigService categoryConfigService;
    private final FileStorageService fileStorageService;  // constructor mein inject karo


    public BusinessCategoryDataService(BusinessRepository businessRepository, BusinessCategoryDataRepository repository,
                                       BusinessCategoryDataMapper mapper, UserRepository userRepository,
                                       CategoryConfigService categoryConfigService,
                                       FileStorageService fileStorageService) {
        this.businessRepository = businessRepository;
        this.repository = repository;
        this.mapper = mapper;
        this.userRepository = userRepository;
        this.categoryConfigService = categoryConfigService;
        this.fileStorageService = fileStorageService;
    }

//    private BusinessEntity getCurrentBusiness() {
//
//        Authentication authentication =
//                SecurityContextHolder.getContext().getAuthentication();
//
//        String email = authentication.getName();
//
//        UserEntity owner =
//                userRepository.findByEmail(email)
//                        .orElseThrow();
//
//        return businessRepository.findByOwner(owner)
//                .orElseThrow();
//    }

//    private void validateField(BusinessEntity business,
//                               String fieldKey) {
//
//        boolean valid = categoryConfigService
//                .isValidField(
//                        business.getCategory(),
//                        fieldKey);
//
//        if (!valid) {
//
//            throw new InvalidCategoryFieldException(fieldKey);
//        }
//    }

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

//    public CategoryRowResponse saveRow(CategoryRowRequest request) {
//
//        BusinessEntity business = getCurrentBusiness();
//
//        Integer sortOrder = getNextSortOrder(business);
//
//        for (Map.Entry<String, String> field : request.getFields().entrySet()) {
//
//            validateField(business, field.getKey());
//
//            BusinessCategoryDataEntity entity =
//                    BusinessCategoryDataEntity.builder()
//                            .business(business)
//                            .fieldKey(field.getKey())
//                            .fieldValue(field.getValue())
//                            .sortOrder(sortOrder)
//                            .build();
//
//            repository.save(entity);
//        }
//
//        return CategoryRowResponse.builder()
//                .sortOrder(sortOrder)
//                .fields(request.getFields())
//                .build();
//    }
//
//    public List<CategoryRowResponse> findRows() {
//
//        BusinessEntity business = getCurrentBusiness();
//
//        List<BusinessCategoryDataEntity> rows =
//                repository.findByBusinessIdOrderBySortOrder(business.getId());
//
//        Map<Integer, Map<String, String>> grouped =
//                new LinkedHashMap<>();
//
//        for (BusinessCategoryDataEntity row : rows) {
//
//            grouped.computeIfAbsent(
//                    row.getSortOrder(),
//                    k -> new LinkedHashMap<>());
//
//            grouped.get(row.getSortOrder())
//                    .put(row.getFieldKey(), row.getFieldValue());
//        }
//
//        return grouped.entrySet()
//                .stream()
//                .map(entry ->
//                        CategoryRowResponse.builder()
//                                .sortOrder(entry.getKey())
//                                .fields(entry.getValue())
//                                .build())
//                .toList();
//    }


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


    public CategoryRowResponse saveRow(CategoryRowRequest request) {

        BusinessEntity business = getCurrentBusiness();

        for (String key : request.getFields().keySet()) {
            validateField(business, key);
        }

        String rowId = UUID.randomUUID().toString();
        Integer sortOrder = getNextSortOrder(business);

        for (Map.Entry<String, String> field : request.getFields().entrySet()) {
            BusinessCategoryDataEntity entity =
                    BusinessCategoryDataEntity.builder()
                            .business(business)
                            .rowId(rowId)
                            .fieldKey(field.getKey())
                            .fieldValue(field.getValue())
                            .sortOrder(sortOrder)
                            .build();
            repository.save(entity);
        }

        return CategoryRowResponse.builder()
                .rowId(rowId)
                .sortOrder(sortOrder)
                .fields(request.getFields())
                .build();
    }

    public List<CategoryRowResponse> findRows() {

        BusinessEntity business = getCurrentBusiness();

        List<BusinessCategoryDataEntity> entities =
                repository.findByBusinessIdOrderBySortOrderAscIdAsc(business.getId());

        // rowId ke basis pe group karo, insertion order preserve karo
        LinkedHashMap<String, List<BusinessCategoryDataEntity>> grouped = new LinkedHashMap<>();
        for (BusinessCategoryDataEntity e : entities) {
            grouped.computeIfAbsent(e.getRowId(), k -> new ArrayList<>()).add(e);
        }

        List<CategoryRowResponse> rows = new ArrayList<>();
        for (Map.Entry<String, List<BusinessCategoryDataEntity>> entry : grouped.entrySet()) {
            Map<String, String> fields = entry.getValue().stream()
                    .collect(Collectors.toMap(
                            BusinessCategoryDataEntity::getFieldKey,
                            BusinessCategoryDataEntity::getFieldValue,
                            (a, b) -> b,
                            LinkedHashMap::new
                    ));

            rows.add(CategoryRowResponse.builder()
                    .rowId(entry.getKey())
                    .sortOrder(entry.getValue().get(0).getSortOrder())
                    .fields(fields)
                    .build());
        }

        return rows;
    }

    public CategoryConfigResponse getConfig() {

        BusinessEntity business = getCurrentBusiness();

        CategoryConfig config = categoryConfigService.load(business.getCategory());

        return CategoryConfigResponse.builder()
                .category(config.getCategory())
                .dashboardSectionLabel(config.getDashboardSectionLabel())
                .fields(config.getFields())
                .build();
    }

    // ---------- Helpers ----------

    private BusinessEntity getCurrentBusiness() {
        UserEntity owner = getLoggedInUser();
        return businessRepository.findByOwnerId(owner.getId())
                .orElseThrow(() -> new RuntimeException("Business not found for logged in user"));
    }

    private UserEntity getLoggedInUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    private Integer getNextSortOrder(BusinessEntity business) {
        return repository.findFirstByBusinessIdOrderBySortOrderDesc(business.getId())
                .map(e -> e.getSortOrder() + 1)
                .orElse(0);
    }

    private void validateField(BusinessEntity business, String key) {
        CategoryConfig config = categoryConfigService.load(business.getCategory());

        boolean isValid = config.getFields().stream()
                .anyMatch(f -> f.getKey().equals(key));

        if (!isValid) {
            throw new InvalidFieldException(key);
        }
    }

    public String uploadItemImage(MultipartFile file) {
        // Ownership check — ensure logged in user has a business
        getCurrentBusiness();
        return fileStorageService.storeCategoryItemImage(file);
    }

    @Transactional
    public void delete(String rowId) {

        BusinessEntity business = getCurrentBusiness();

        List<BusinessCategoryDataEntity> existing =
                repository.findByBusinessIdAndRowId(business.getId(), rowId);

        if (existing.isEmpty()) {
            throw new RowNotFoundException(rowId);
        }

        deleteImageFieldsIfAny(business, existing);

        repository.deleteByBusinessIdAndRowId(business.getId(), rowId);
    }

    @Transactional
    public CategoryRowResponse updateRow(String rowId, CategoryRowRequest request) {

        BusinessEntity business = getCurrentBusiness();

        List<BusinessCategoryDataEntity> existing =
                repository.findByBusinessIdAndRowId(business.getId(), rowId);

        if (existing.isEmpty()) {
            throw new RowNotFoundException(rowId);
        }

        Integer sortOrder = existing.get(0).getSortOrder();

        for (String key : request.getFields().keySet()) {
            validateField(business, key);
        }

        // Agar image field ki value change hui hai (naya path aaya), purani image delete karo
        deleteReplacedImageFields(business, existing, request.getFields());

        repository.deleteAll(existing);

        for (Map.Entry<String, String> field : request.getFields().entrySet()) {
            BusinessCategoryDataEntity entity =
                    BusinessCategoryDataEntity.builder()
                            .business(business)
                            .rowId(rowId)
                            .fieldKey(field.getKey())
                            .fieldValue(field.getValue())
                            .sortOrder(sortOrder)
                            .build();
            repository.save(entity);
        }

        return CategoryRowResponse.builder()
                .rowId(rowId)
                .sortOrder(sortOrder)
                .fields(request.getFields())
                .build();
    }

// ---------- Helper: image cleanup ----------

    private void deleteImageFieldsIfAny(BusinessEntity business, List<BusinessCategoryDataEntity> entities) {
        Set<String> imageKeys = getImageFieldKeys(business);

        for (BusinessCategoryDataEntity e : entities) {
            if (imageKeys.contains(e.getFieldKey())) {
                fileStorageService.delete(e.getFieldValue());
            }
        }
    }

    private void deleteReplacedImageFields(
            BusinessEntity business,
            List<BusinessCategoryDataEntity> oldEntities,
            Map<String, String> newFields) {

        Set<String> imageKeys = getImageFieldKeys(business);

        for (BusinessCategoryDataEntity old : oldEntities) {
            if (imageKeys.contains(old.getFieldKey())) {
                String newValue = newFields.get(old.getFieldKey());
                // Agar naya path alag hai purane se, to purani image delete karo
                if (newValue != null && !newValue.equals(old.getFieldValue())) {
                    fileStorageService.delete(old.getFieldValue());
                }
            }
        }
    }

    private Set<String> getImageFieldKeys(BusinessEntity business) {
        CategoryConfig config = categoryConfigService.load(business.getCategory());
        return config.getFields().stream()
                .filter(f -> "image".equals(f.getType()))
                .map(CategoryField::getKey)
                .collect(Collectors.toSet());
    }
}