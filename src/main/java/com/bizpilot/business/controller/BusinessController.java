package com.bizpilot.business.controller;

import com.bizpilot.business.model.Business;
import com.bizpilot.business.model.BusinessCategory;
import com.bizpilot.business.model.CategoryConfig;
import com.bizpilot.business.service.CategoryConfigService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.Map;

@RestController
public class BusinessController {

    private final CategoryConfigService categoryConfigService;

    public BusinessController(CategoryConfigService categoryConfigService) {
        this.categoryConfigService = categoryConfigService;
    }


}