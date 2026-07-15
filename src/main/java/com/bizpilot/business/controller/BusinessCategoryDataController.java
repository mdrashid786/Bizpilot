package com.bizpilot.business.controller;

import com.bizpilot.business.dto.request.CategoryDataRequest;
import com.bizpilot.business.dto.request.CategoryRowRequest;
import com.bizpilot.business.dto.response.CategoryDataResponse;
import com.bizpilot.business.dto.response.CategoryRowResponse;
import com.bizpilot.business.service.BusinessCategoryDataService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/business/category-data")
@RequiredArgsConstructor
public class BusinessCategoryDataController {

    private final BusinessCategoryDataService service;

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

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {

        service.delete(id);
    }

    @PostMapping("/row")
    public CategoryRowResponse saveRow(
            @Valid @RequestBody CategoryRowRequest request) {

        return service.saveRow(request);
    }

    @GetMapping("/rows")
    public List<CategoryRowResponse> findRows() {

        return service.findRows();
    }

    @PutMapping("/row/{sortOrder}")
    public CategoryRowResponse updateRow(

            @PathVariable Integer sortOrder,

            @Valid @RequestBody CategoryRowRequest request) {

        return service.updateRow(sortOrder, request);
    }
}