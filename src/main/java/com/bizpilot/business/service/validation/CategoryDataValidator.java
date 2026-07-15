package com.bizpilot.business.service.validation;

import com.bizpilot.business.dto.request.MenuItemRequest;
import com.bizpilot.business.model.CategoryConfig;
import com.bizpilot.business.model.CategoryField;
import org.springframework.stereotype.Component;

@Component
public class CategoryDataValidator {

    public void validate(CategoryConfig config,
                         MenuItemRequest request) {

        // Required Fields
        for (CategoryField field : config.getFields()) {

            if (!request.getValues().containsKey(field.getKey())) {

                throw new RuntimeException(
                        "Missing field : " + field.getKey());
            }

            validateType(field, request.getValues().get(field.getKey()));
        }

        // Extra Fields
        for (String key : request.getValues().keySet()) {

            boolean exists = config.getFields()
                    .stream()
                    .anyMatch(f -> f.getKey().equals(key));

            if (!exists) {

                throw new RuntimeException(
                        "Invalid field : " + key);
            }
        }

    }

    private void validateType(CategoryField field,
                              String value) {

        switch (field.getType()) {

            case "number" -> {

                try {

                    Integer.parseInt(value);

                } catch (NumberFormatException ex) {

                    throw new RuntimeException(
                            field.getLabel() + " must be a number");
                }
            }

            case "text" -> {

                if (value == null || value.isBlank()) {

                    throw new RuntimeException(
                            field.getLabel() + " is required");
                }
            }
        }
    }

}