package com.bizpilot.business.controller;

import com.bizpilot.business.dto.request.BusinessRegistrationRequest;
import com.bizpilot.business.dto.response.BusinessResponse;
import com.bizpilot.business.mapper.BusinessMapper;
import com.bizpilot.business.model.Business;
import com.bizpilot.business.model.CategoryConfig;
import com.bizpilot.business.service.BusinessService;
import com.bizpilot.business.service.CategoryConfigService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.Map;

@RestController
public class BusinessController {

    private final BusinessService businessService;

    private final BusinessMapper businessMapper;

    public BusinessController(BusinessService businessService,
                              BusinessMapper businessMapper){

        this.businessService=businessService;
        this.businessMapper=businessMapper;
    }

    @PostMapping
    public ResponseEntity<BusinessResponse> register(

            @Valid @RequestBody BusinessRegistrationRequest request){

        Business business=businessService.register(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(businessMapper.toResponse(business));
    }

    @GetMapping("/{slug}")
    public ResponseEntity<BusinessResponse> getBusiness(

            @PathVariable String slug){

        Business business=businessService.findBySlug(slug);

        return ResponseEntity.ok(

                businessMapper.toResponse(business)
        );
    }


}