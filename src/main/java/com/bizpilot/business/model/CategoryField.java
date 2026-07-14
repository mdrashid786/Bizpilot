package com.bizpilot.business.model;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryField {

    private String key;
    private String label;
    private String type;

}
