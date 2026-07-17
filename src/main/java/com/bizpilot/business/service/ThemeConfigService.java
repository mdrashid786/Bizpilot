package com.bizpilot.business.service;

import com.bizpilot.business.model.BusinessCategory;
import com.bizpilot.business.model.ThemeOption;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Service
public class ThemeConfigService {

    private final ObjectMapper objectMapper = new ObjectMapper();

    public List<ThemeOption> loadThemes(BusinessCategory category) {
        try {
            String fileName = category.name().toLowerCase() + ".json";
            String path = "themes/" + fileName;

            ClassPathResource resource = new ClassPathResource(path);
            InputStream inputStream = resource.getInputStream();

            return objectMapper.readValue(inputStream, objectMapper.getTypeFactory()
                    .constructCollectionType(List.class, ThemeOption.class));

        } catch (IOException e) {
            throw new RuntimeException("Unable to load themes for category: " + category, e);
        }
    }
}