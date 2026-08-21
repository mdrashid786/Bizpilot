package com.bizpilot.business.controller;

import com.bizpilot.business.dto.response.OrderStatsResponse;
import com.bizpilot.business.dto.response.RecentOrderResponse;
import com.bizpilot.business.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/business/orders")
@RequiredArgsConstructor
public class BusinessOrderController {

    private final OrderService orderService;

    @GetMapping("/recent")
    public List<RecentOrderResponse> getRecentOrders() {
        return orderService.getRecentOrders();
    }

    @GetMapping("/stats")
    public OrderStatsResponse getStats() {
        return orderService.getStats();
    }
}