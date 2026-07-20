package com.bizpilot.business.service;


import com.bizpilot.business.entity.BusinessCategoryDataEntity;
import com.bizpilot.business.entity.BusinessEntity;
import com.bizpilot.business.model.CategoryConfig;
import com.bizpilot.business.model.CategoryField;
import com.bizpilot.business.repository.BusinessCategoryDataRepository;
import com.bizpilot.common.exception.BusinessNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.*;

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
//        BusinessEntity business = businessService.getBusinessBySlug(slug);
//
//        CategoryConfig config = categoryConfigService.load(business.getCategory());
//
//        List<BusinessCategoryDataEntity> rows =
//                categoryDataRepository.findByBusinessOrderBySortOrder(business);
//
//        Map<Integer, Map<String, String>> grouped = new LinkedHashMap<>();
//
//        for (BusinessCategoryDataEntity row : rows) {
//            grouped.computeIfAbsent(row.getSortOrder(), key -> new LinkedHashMap<>());
//            grouped.get(row.getSortOrder()).put(row.getFieldKey(), row.getFieldValue());
//        }
//
//        // Generic field-key resolution — template ko exact key naam nahi pata hona chahiye
//        String nameKey = config.getFields().stream()
//                .filter(f -> "text".equals(f.getType()))
//                .map(CategoryField::getKey)
//                .findFirst()
//                .orElse(null);
//
//        String priceKey = config.getFields().stream()
//                .filter(f -> "number".equals(f.getType()))
//                .map(CategoryField::getKey)
//                .findFirst()
//                .orElse(null);
//
//        String imageKey = config.getFields().stream()
//                .filter(f -> "image".equals(f.getType()))
//                .map(CategoryField::getKey)
//                .findFirst()
//                .orElse(null);
//
//        model.addAttribute("business", business);
//        model.addAttribute("config", config);
//        model.addAttribute("categoryData", grouped.values());
//        model.addAttribute("nameKey", nameKey);
//        model.addAttribute("priceKey", priceKey);
//        model.addAttribute("imageKey", imageKey);
//        System.out.println("theme : "+business.getTheme());
//        return business.getTheme() + "/index";
//    }

    public String render(String slug, Model model) {

        BusinessEntity business = businessService.getBusinessBySlug(slug);
        CategoryConfig config = categoryConfigService.load(business.getCategory());

        List<BusinessCategoryDataEntity> rows =
                categoryDataRepository.findByBusinessOrderBySortOrder(business);

        Map<Integer, Map<String, String>> grouped = new LinkedHashMap<>();
        Set<Integer> featuredSortOrders = new HashSet<>();

        for (BusinessCategoryDataEntity row : rows) {

            if (!Boolean.TRUE.equals(row.getActive())) {
                continue; // inactive items website pe kabhi nahi
            }

            grouped.computeIfAbsent(row.getSortOrder(), key -> new LinkedHashMap<>());
            grouped.get(row.getSortOrder()).put(row.getFieldKey(), row.getFieldValue());

            if (Boolean.TRUE.equals(row.getFeatured())) {
                featuredSortOrders.add(row.getSortOrder());
            }
        }

        List<Map<String, String>> rowsList = new ArrayList<>(grouped.values());

        // Generic field-key resolution
        List<String> textKeys = config.getFields().stream()
                .filter(f -> "text".equals(f.getType()))
                .map(CategoryField::getKey)
                .toList();

        String nameKey = textKeys.isEmpty() ? null : textKeys.get(0);
        String descriptionKey = textKeys.size() > 1 ? textKeys.get(1) : null;

        String priceKey = config.getFields().stream()
                .filter(f -> "number".equals(f.getType()))
                .map(CategoryField::getKey).findFirst().orElse(null);

        String imageKey = config.getFields().stream()
                .filter(f -> "image".equals(f.getType()))
                .map(CategoryField::getKey).findFirst().orElse(null);

        String typeKey = config.getFields().stream()
                .filter(f -> "select".equals(f.getType()))
                .map(CategoryField::getKey).findFirst().orElse(null);

        List<String> typeOptions = config.getFields().stream()
                .filter(f -> "select".equals(f.getType()))
                .findFirst()
                .map(CategoryField::getOptions)
                .orElse(List.of());

        // 👇 ASLI FIX — owner ne jo explicitly "featured" mark kiya hai, sirf wahi
        List<Map<String, String>> specialItems = grouped.entrySet().stream()
                .filter(entry -> featuredSortOrders.contains(entry.getKey()))
                .map(Map.Entry::getValue)
                .toList();

        model.addAttribute("business", business);
        model.addAttribute("config", config);
        model.addAttribute("categoryData", rowsList);
        model.addAttribute("specialItems", specialItems);
        model.addAttribute("nameKey", nameKey);
        model.addAttribute("descriptionKey", descriptionKey);
        model.addAttribute("priceKey", priceKey);
        model.addAttribute("imageKey", imageKey);
        model.addAttribute("typeKey", typeKey);
        model.addAttribute("typeOptions", typeOptions);

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
