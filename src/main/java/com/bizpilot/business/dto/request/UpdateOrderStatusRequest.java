package com.bizpilot.business.dto.request;

import com.bizpilot.business.model.OrderStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

// UpdateOrderStatusRequest.java
@Getter
@Setter
public class UpdateOrderStatusRequest {
    @NotNull
    private OrderStatus status;
}