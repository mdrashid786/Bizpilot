package com.bizpilot.business.controller;

import com.bizpilot.business.dto.request.BusinessRegistrationRequest;
import com.bizpilot.business.dto.request.BusinessUpdateRequest;
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
import org.springframework.web.multipart.MultipartFile;

import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/business")
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

    @GetMapping("/my")
    public ResponseEntity<BusinessResponse> getMyBusiness() {

        Business business = businessService.getMyBusiness();

        if (business == null) {
            return ResponseEntity.noContent().build(); // 204 - business nahi bana abhi tak
        }

        System.out.println("bbb : "+business.getBusinessName());

        return ResponseEntity.ok(businessMapper.toResponse(business));
    }

    @PutMapping("/{id}")
    public ResponseEntity<BusinessResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody BusinessUpdateRequest request) {

        Business business = businessService.update(id, request);

        return ResponseEntity.ok(businessMapper.toResponse(business));
    }
    @PostMapping("/{id}/logo")
    public ResponseEntity<BusinessResponse> uploadLogo(
            @PathVariable Long id,
            @RequestParam("file") MultipartFile file) {

        Business business = businessService.uploadLogo(id, file);
        return ResponseEntity.ok(businessMapper.toResponse(business));
    }

    @PostMapping("/{id}/cover-image")
    public ResponseEntity<BusinessResponse> uploadCoverImage(
            @PathVariable Long id,
            @RequestParam("file") MultipartFile file) {

        Business business = businessService.uploadCoverImage(id, file);
        return ResponseEntity.ok(businessMapper.toResponse(business));
    }

    @PatchMapping("/{id}/publish")
    public ResponseEntity<BusinessResponse> togglePublish(@PathVariable Long id) {
        Business business = businessService.togglePublish(id);
        return ResponseEntity.ok(businessMapper.toResponse(business));
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