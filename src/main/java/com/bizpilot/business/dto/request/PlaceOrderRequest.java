package com.bizpilot.business.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PlaceOrderRequest {

    @NotBlank
    private String customerName;

    @NotBlank
    private String customerPhone;

    @NotEmpty
    private List<OrderItem> items;

    private Boolean diningIn;

    private String tableNumber;

    @Getter
    @Setter
    public static class OrderItem {
        private String name;
        private Double price;
        private Integer qty;
    }
}