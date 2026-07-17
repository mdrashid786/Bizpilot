package com.bizpilot.business.model;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class CategoryField {

    private String key;
    private String label;
    private String type;
    private List<String> options; // sirf "select" type ke liye use hoga, baaki mein null rahega


}
