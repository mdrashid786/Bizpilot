package com.bizpilot.business.model;


import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryConfig {

    private BusinessCategory category;
    private String template;
    private String dashboardSectionLabel;
    private List<CategoryField> fields;


}
