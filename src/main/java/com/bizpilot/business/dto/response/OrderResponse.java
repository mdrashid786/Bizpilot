package com.bizpilot.business.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class OrderResponse {
    private Long id;
    private String message;
}