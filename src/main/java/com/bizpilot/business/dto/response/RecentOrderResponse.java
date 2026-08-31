package com.bizpilot.business.dto.response;

import com.bizpilot.business.model.OrderStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class RecentOrderResponse {
    private Long id;
    private String customerName;
    private String customerPhone;
    private String itemsJson;
    private Double totalAmount;
    private Boolean diningIn;
    private String tableNumber;
    private OrderStatus status; // 👈 Ensure OrderStatus type yahan maujood hai
    private LocalDateTime createdAt;
}