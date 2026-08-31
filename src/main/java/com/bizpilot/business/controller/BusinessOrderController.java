package com.bizpilot.business.controller;

import com.bizpilot.business.dto.request.UpdateOrderStatusRequest;
import com.bizpilot.business.dto.response.OrderStatsResponse;
import com.bizpilot.business.dto.response.RecentOrderResponse;
import com.bizpilot.business.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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

    @PatchMapping("/{id}/status")
    public ResponseEntity<Void> updateStatus(@PathVariable Long id, @Valid @RequestBody UpdateOrderStatusRequest request) {
        orderService.updateStatus(id, request.getStatus());
        return ResponseEntity.ok().build();
    }
}