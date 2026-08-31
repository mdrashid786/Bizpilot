package com.bizpilot.business.dto.response;

import com.bizpilot.business.model.OrderStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

// OrderDetailResponse.java
@Getter
@Builder
public class OrderDetailResponse {
    private Long id;
    private String customerName;
    private String itemsJson;
    private Double totalAmount;
    private String tableNumber;
    private OrderStatus status;
    private LocalDateTime createdAt;
}