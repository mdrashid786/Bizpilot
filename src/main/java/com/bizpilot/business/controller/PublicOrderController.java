package com.bizpilot.business.controller;

import com.bizpilot.business.dto.request.PlaceOrderRequest;
import com.bizpilot.business.dto.response.OrderDetailResponse;
import com.bizpilot.business.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/public/orders")
@RequiredArgsConstructor
public class PublicOrderController {

    private final OrderService orderService;

//    @PostMapping("/{slug}")
//    public ResponseEntity<Void> placeOrder(
//            @PathVariable String slug,
//            @Valid @RequestBody PlaceOrderRequest request) {
//
//        orderService.placeOrder(slug, request);
//        System.out.println("Hello Rashid : "+request.getCustomerName());
//        return ResponseEntity.ok().build();
//    }

    // PublicOrderController.java
    @PostMapping("/{slug}")
    public ResponseEntity<Map<String, Long>> placeOrder(@PathVariable String slug, @Valid @RequestBody PlaceOrderRequest request) {
        Long orderId = orderService.placeOrder(slug, request);
        return ResponseEntity.ok(Map.of("orderId", orderId));
    }

    @GetMapping("/detail/{orderId}")
    public OrderDetailResponse getOrderDetail(@PathVariable Long orderId) {
        return orderService.getOrderDetail(orderId);
    }
}