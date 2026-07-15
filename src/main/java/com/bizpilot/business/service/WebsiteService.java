package com.bizpilot.business.service;


import com.bizpilot.business.entity.BusinessCategoryDataEntity;
import com.bizpilot.business.entity.BusinessEntity;
import com.bizpilot.business.model.CategoryConfig;
import com.bizpilot.business.repository.BusinessCategoryDataRepository;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class WebsiteService {

    private  final BusinessService businessService;
    private final BusinessCategoryDataRepository categoryDataRepository;
    private final CategoryConfigService categoryConfigService;

    public WebsiteService(BusinessService businessService, BusinessCategoryDataRepository categoryDataRepository,
                          CategoryConfigService categoryConfigService) {
        this.businessService = businessService;
        this.categoryDataRepository = categoryDataRepository;
        this.categoryConfigService = categoryConfigService;
    }

    public String render(String slug, Model model) {

        BusinessEntity business =
                businessService.getBusinessBySlug(slug);

        CategoryConfig config =
                categoryConfigService.load(business.getCategory());

        List<BusinessCategoryDataEntity> rows =
                categoryDataRepository.findByBusinessOrderBySortOrder(business);

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

        model.addAttribute("business", business);
        model.addAttribute("config", config);
        model.addAttribute("categoryData", grouped.values());

        return business.getTheme() + "/index";
    }

}
