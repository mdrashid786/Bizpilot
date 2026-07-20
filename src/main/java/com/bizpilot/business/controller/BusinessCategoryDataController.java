package com.bizpilot.business.controller;

import com.bizpilot.business.dto.request.CategoryDataRequest;
import com.bizpilot.business.dto.request.CategoryRowRequest;
import com.bizpilot.business.dto.response.CategoryConfigResponse;
import com.bizpilot.business.dto.response.CategoryDataResponse;
import com.bizpilot.business.dto.response.CategoryRowResponse;
import com.bizpilot.business.service.BusinessCategoryDataService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/business/category-data")
@RequiredArgsConstructor
public class BusinessCategoryDataController {

    private final BusinessCategoryDataService service;


    @GetMapping("/config")
    public CategoryConfigResponse getConfig() {
        return service.getConfig();
    }

    @PostMapping("/row")
    public CategoryRowResponse saveRow(@Valid @RequestBody CategoryRowRequest request) {
        return service.saveRow(request);
    }

    @GetMapping("/rows")
    public List<CategoryRowResponse> findRows() {
        return service.findRows();
    }

    @PutMapping("/row/{rowId}")
    public CategoryRowResponse updateRow(
            @PathVariable String rowId,
            @Valid @RequestBody CategoryRowRequest request) {
        return service.updateRow(rowId, request);
    }

    @DeleteMapping("/row/{rowId}")
    public void delete(@PathVariable String rowId) {
        service.delete(rowId);
    }
    @PostMapping("/image")
    public Map<String, String> uploadImage(@RequestParam("file") MultipartFile file) {
        String path = service.uploadItemImage(file);
        return Map.of("path", path);
    }


    // old one
    @PostMapping
    public CategoryDataResponse save(
            @Valid @RequestBody CategoryDataRequest request) {

        return service.save(request);
    }

    @GetMapping
    public List<CategoryDataResponse> findAll() {

        return service.findAll();
    }

    @PutMapping("/{id}")
    public CategoryDataResponse update(
            @PathVariable Long id,
            @Valid @RequestBody CategoryDataRequest request) {

        return service.update(id, request);
    }

    // BusinessCategoryDataController.java mein:
    @PatchMapping("/row/{rowId}/toggle-active")
    public CategoryRowResponse toggleActive(@PathVariable String rowId) {
        return service.toggleActive(rowId);
    }

    // Controller mein:
    @PatchMapping("/row/{rowId}/toggle-featured")
    public CategoryRowResponse toggleFeatured(@PathVariable String rowId) {
        return service.toggleFeatured(rowId);
    }
}