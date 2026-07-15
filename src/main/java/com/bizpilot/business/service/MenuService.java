package com.bizpilot.business.service;


import com.bizpilot.business.dto.request.MenuItemRequest;
import com.bizpilot.business.dto.response.MenuItemResponse;
import com.bizpilot.business.entity.BusinessCategoryDataEntity;
import com.bizpilot.business.entity.BusinessEntity;
import com.bizpilot.business.model.CategoryConfig;
import com.bizpilot.business.model.CategoryField;
import com.bizpilot.business.repository.BusinessCategoryDataRepository;
import com.bizpilot.business.service.validation.CategoryDataValidator;
import com.bizpilot.common.exception.CategoryDataNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class MenuService {

    private final BusinessService businessService;
    private final BusinessCategoryDataRepository repository;
    private final CategoryConfigService categoryConfigService;
    private final CategoryDataValidator validator;

    public MenuService(BusinessService businessService, BusinessCategoryDataRepository repository,
                       CategoryConfigService categoryConfigService, CategoryDataValidator validator) {
        this.businessService = businessService;
        this.repository = repository;
        this.categoryConfigService = categoryConfigService;
        this.validator = validator;

    }

    public MenuItemResponse add(MenuItemRequest request) {

        BusinessEntity business =
                businessService.getCurrentBusinessEntity();

        CategoryConfig config =
                categoryConfigService.load(business.getCategory());

        Integer count =
                repository.countByBusiness(business);

        Integer sortOrder = (count / config.getFields().size()) + 1;

        // Validation
//        for (CategoryField field : config.getFields()) {
//
//            if (!request.getValues().containsKey(field.getKey())) {
//
//                throw new RuntimeException(
//                        "Missing field : " + field.getKey());
//            }
//        }
        validator.validate(config, request);

        // Save Dynamic Fields
        for (CategoryField field : config.getFields()) {

            BusinessCategoryDataEntity entity =
                    BusinessCategoryDataEntity.builder()
                            .business(business)
                            .fieldKey(field.getKey())
                            .fieldValue(
                                    request.getValues()
                                            .get(field.getKey()))
                            .sortOrder(sortOrder)
                            .build();

            repository.save(entity);
        }

        return MenuItemResponse.builder()
                .sortOrder(sortOrder)
                .values(request.getValues())
                .build();
    }

    public List<MenuItemResponse> getMenu() {

        BusinessEntity business =
                businessService.getCurrentBusinessEntity();

        List<BusinessCategoryDataEntity> rows =
                repository.findByBusinessOrderBySortOrder(business);


        Map<Integer, Map<String, String>> grouped =
                new LinkedHashMap<>();

        for (BusinessCategoryDataEntity row : rows) {

            grouped.computeIfAbsent(
                    row.getSortOrder(),
                    key -> new LinkedHashMap<>());

            grouped.get(row.getSortOrder())
                    .put(
                            row.getFieldKey(),
                            row.getFieldValue()
                    );
        }

        List<MenuItemResponse> response =
                new ArrayList<>();

        for (Map.Entry<Integer, Map<String, String>> entry
                : grouped.entrySet()) {

            response.add(

                    MenuItemResponse.builder()
                            .sortOrder(entry.getKey())
                            .values(entry.getValue())
                            .build()

            );
        }

        return response;
    }

    public MenuItemResponse update(Integer sortOrder,
                                   MenuItemRequest request) {

        BusinessEntity business =
                businessService.getCurrentBusinessEntity();

        CategoryConfig config =
                categoryConfigService.load(business.getCategory());

        validator.validate(config, request);

        List<BusinessCategoryDataEntity> rows =
                repository.findByBusinessAndSortOrder(
                        business,
                        sortOrder);

        if (rows.isEmpty()) {
            throw new CategoryDataNotFoundException(sortOrder);
        }

        Map<String, BusinessCategoryDataEntity> existing =
                rows.stream()
                        .collect(Collectors.toMap(
                                BusinessCategoryDataEntity::getFieldKey,
                                Function.identity()
                        ));

        for (CategoryField field : config.getFields()) {

            BusinessCategoryDataEntity entity =
                    existing.get(field.getKey());

            entity.setFieldValue(
                    request.getValues()
                            .get(field.getKey()));

            repository.save(entity);
        }

        return MenuItemResponse.builder()
                .sortOrder(sortOrder)
                .values(request.getValues())
                .build();
    }

    @Transactional
    public void delete(Integer sortOrder) {

        BusinessEntity business =
                businessService.getCurrentBusinessEntity();

        List<BusinessCategoryDataEntity> rows =
                repository.findByBusinessAndSortOrder(
                        business,
                        sortOrder);

        if (rows.isEmpty()) {

            throw new CategoryDataNotFoundException(sortOrder);
        }

        repository.deleteByBusinessAndSortOrder(
                business,
                sortOrder);
    }
}