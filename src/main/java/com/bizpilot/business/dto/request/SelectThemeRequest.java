package com.bizpilot.business.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SelectThemeRequest {

    @NotBlank
    private String theme;
}