package com.bizpilot.business.controller;

import com.bizpilot.business.dto.response.DashboardResponse;
import com.bizpilot.business.mapper.BusinessMapper;
import com.bizpilot.business.model.Business;
import com.bizpilot.business.service.BusinessService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DashboardController {

    private final BusinessService businessService;
    private final BusinessMapper businessMapper;

    public DashboardController(BusinessService businessService, BusinessMapper businessMapper) {
        this.businessService = businessService;
        this.businessMapper = businessMapper;
    }

    @GetMapping("/api/dashboard")
    public DashboardResponse dashboard() {

        Business business = businessService.getMyBusiness();

        return DashboardResponse.builder()
                .businessCreated(true)
                .business(
                        businessMapper.toResponse(business)
                )
                .build();
    }

}