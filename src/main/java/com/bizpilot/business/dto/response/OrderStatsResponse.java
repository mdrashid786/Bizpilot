package com.bizpilot.business.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class OrderStatsResponse {
    private Long totalOrders;
    private Double totalRevenue;
    private Long todayOrders;
    private Double todayRevenue;
    private Long totalCustomers;
}