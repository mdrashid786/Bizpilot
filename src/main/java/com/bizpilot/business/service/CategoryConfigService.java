package com.bizpilot.business.service;

import java.io.IOException;
import java.io.InputStream;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import com.bizpilot.business.model.BusinessCategory;
import com.bizpilot.business.model.CategoryConfig;

import com.fasterxml.jackson.databind.ObjectMapper; 
 
@Service
public class CategoryConfigService { 

    private final ObjectMapper objectMapper;

    public CategoryConfigService(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public CategoryConfig load(BusinessCategory category) {

        try {

            String fileName = category.name().toLowerCase() + ".json";

            String path = "category-config/" + fileName;

            ClassPathResource resource = new ClassPathResource(path);

            InputStream inputStream = resource.getInputStream();

            return objectMapper.readValue(inputStream, CategoryConfig.class);

        } catch (IOException e) {
            throw new RuntimeException("Unable to load category config : " + category, e);
        }
    }

    public boolean isValidField(BusinessCategory category, String fieldKey) {

        CategoryConfig config = load(category);

        return config.getFields()
                .stream()
                .anyMatch(field ->
                        field.getKey().equals(fieldKey));
    }
}