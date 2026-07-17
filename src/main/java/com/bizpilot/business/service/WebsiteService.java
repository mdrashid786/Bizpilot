package com.bizpilot.business.service;


import com.bizpilot.business.entity.BusinessCategoryDataEntity;
import com.bizpilot.business.entity.BusinessEntity;
import com.bizpilot.business.model.CategoryConfig;
import com.bizpilot.business.model.CategoryField;
import com.bizpilot.business.repository.BusinessCategoryDataRepository;
import com.bizpilot.common.exception.BusinessNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

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

    private static final Set<String> RESERVED_SLUGS = Set.of(
            "api", "uploads", "themes", "error", "css", "js", "images",
            "favicon.ico", "robots.txt", "sitemap.xml"
    );


//    public String render(String slug, Model model) {
//
//        if (RESERVED_SLUGS.contains(slug.toLowerCase())) {
//            throw new BusinessNotFoundException(slug); // ya apna 404 handler
//        }
//
//        BusinessEntity business =
//                businessService.getBusinessBySlug(slug);
//
//        CategoryConfig config =
//                categoryConfigService.load(business.getCategory());
//
//        List<BusinessCategoryDataEntity> rows =
//                categoryDataRepository.findByBusinessOrderBySortOrder(business);
//
//        Map<Integer, Map<String, String>> grouped =
//                new LinkedHashMap<>();
//
//        for (BusinessCategoryDataEntity row : rows) {
//
//            grouped.computeIfAbsent(
//                    row.getSortOrder(),
//                    key -> new LinkedHashMap<>());
//
//            grouped.get(row.getSortOrder())
//                    .put(
//                            row.getFieldKey(),
//                            row.getFieldValue()
//                    );
//        }
//
//        model.addAttribute("business", business);
//        model.addAttribute("config", config);
//        model.addAttribute("categoryData", grouped.values());
//
//        System.out.println("theme : "+business.getTheme());
//        return business.getTheme() + "/index";
//    }


    public String render(String slug, Model model) {

        BusinessEntity business = businessService.getBusinessBySlug(slug);

        CategoryConfig config = categoryConfigService.load(business.getCategory());

        List<BusinessCategoryDataEntity> rows =
                categoryDataRepository.findByBusinessOrderBySortOrder(business);

        Map<Integer, Map<String, String>> grouped = new LinkedHashMap<>();

        for (BusinessCategoryDataEntity row : rows) {
            grouped.computeIfAbsent(row.getSortOrder(), key -> new LinkedHashMap<>());
            grouped.get(row.getSortOrder()).put(row.getFieldKey(), row.getFieldValue());
        }

        // Generic field-key resolution — template ko exact key naam nahi pata hona chahiye
        String nameKey = config.getFields().stream()
                .filter(f -> "text".equals(f.getType()))
                .map(CategoryField::getKey)
                .findFirst()
                .orElse(null);

        String priceKey = config.getFields().stream()
                .filter(f -> "number".equals(f.getType()))
                .map(CategoryField::getKey)
                .findFirst()
                .orElse(null);

        String imageKey = config.getFields().stream()
                .filter(f -> "image".equals(f.getType()))
                .map(CategoryField::getKey)
                .findFirst()
                .orElse(null);

        model.addAttribute("business", business);
        model.addAttribute("config", config);
        model.addAttribute("categoryData", grouped.values());
        model.addAttribute("nameKey", nameKey);
        model.addAttribute("priceKey", priceKey);
        model.addAttribute("imageKey", imageKey);
        System.out.println("theme : "+business.getTheme());
        return business.getTheme() + "/index";
    }

    public String renderWithTheme(String slug, String themeOverride, Model model) {

        BusinessEntity business = businessService.getBusinessBySlug(slug);
        // Note: preview ke liye draft business bhi dikhni chahiye, isliye "IgnorePublished" version chahiye

        CategoryConfig config = categoryConfigService.load(business.getCategory());

        List<BusinessCategoryDataEntity> rows =
                categoryDataRepository.findByBusinessOrderBySortOrder(business);

        Map<Integer, Map<String, String>> grouped = new LinkedHashMap<>();
        for (BusinessCategoryDataEntity row : rows) {
            grouped.computeIfAbsent(row.getSortOrder(), key -> new LinkedHashMap<>());
            grouped.get(row.getSortOrder()).put(row.getFieldKey(), row.getFieldValue());
        }

        String nameKey = config.getFields().stream().filter(f -> "text".equals(f.getType())).map(CategoryField::getKey).findFirst().orElse(null);
        String priceKey = config.getFields().stream().filter(f -> "number".equals(f.getType())).map(CategoryField::getKey).findFirst().orElse(null);
        String imageKey = config.getFields().stream().filter(f -> "image".equals(f.getType())).map(CategoryField::getKey).findFirst().orElse(null);

        model.addAttribute("business", business);
        model.addAttribute("config", config);
        model.addAttribute("categoryData", grouped.values());
        model.addAttribute("nameKey", nameKey);
        model.addAttribute("priceKey", priceKey);
        model.addAttribute("imageKey", imageKey);

        return themeOverride + "/index"; // saved theme ki jagah override use karo
    }
}
