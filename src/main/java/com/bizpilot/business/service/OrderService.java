package com.bizpilot.business.service;

import com.bizpilot.authentication.entity.UserEntity;
import com.bizpilot.authentication.repository.UserRepository;
import com.bizpilot.business.dto.request.PlaceOrderRequest;
import com.bizpilot.business.dto.response.OrderStatsResponse;
import com.bizpilot.business.dto.response.RecentOrderResponse;
import com.bizpilot.business.entity.BusinessEntity;
import com.bizpilot.business.entity.CustomerEntity;
import com.bizpilot.business.entity.OrderEntity;
import com.bizpilot.business.repository.BusinessRepository;
import com.bizpilot.business.repository.CustomerRepository;
import com.bizpilot.business.repository.OrderRepository;
import com.bizpilot.business.service.BusinessService;
import com.bizpilot.common.exception.BusinessNotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final CustomerRepository customerRepository;
    private final BusinessRepository businessRepository;
    private final UserRepository userRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Transactional
    public void placeOrder(String slug, PlaceOrderRequest request) {

        BusinessEntity business = businessRepository.findBySlug(slug)
                .orElseThrow(() -> new BusinessNotFoundException(slug));

        double total = request.getItems().stream()
                .mapToDouble(i -> i.getPrice() * i.getQty())
                .sum();

        // Customer find-or-create (same phone => ek hi entry, business ke andar)
        CustomerEntity customer = customerRepository
                .findByBusinessAndPhone(business, request.getCustomerPhone())
                .orElseGet(() -> CustomerEntity.builder()
                        .business(business)
                        .phone(request.getCustomerPhone())
                        .name(request.getCustomerName())
                        .totalOrders(0)
                        .totalSpent(0.0)
                        .build());

        customer.setName(request.getCustomerName()); // latest naam save karo
        customer.setTotalOrders(customer.getTotalOrders() + 1);
        customer.setTotalSpent(customer.getTotalSpent() + total);
        customer = customerRepository.save(customer);

        String itemsJson;
        try {
            itemsJson = objectMapper.writeValueAsString(request.getItems());
        } catch (Exception e) {
            throw new RuntimeException("Failed to serialize order items", e);
        }

        OrderEntity order = OrderEntity.builder()
                .business(business)
                .customer(customer)
                .customerName(request.getCustomerName())
                .customerPhone(request.getCustomerPhone())
                .itemsJson(itemsJson)
                .totalAmount(total)
                .diningIn(request.getDiningIn())
                .tableNumber(request.getTableNumber())
                .build();

        orderRepository.save(order);
    }

    public List<RecentOrderResponse> getRecentOrders() {

        BusinessEntity business = getOwnedBusinessOfCurrentUser();

        List<OrderEntity> orders = orderRepository.findTop20ByBusinessOrderByCreatedAtDesc(business);

        return orders.stream().map(o -> RecentOrderResponse.builder()
                .id(o.getId())
                .customerName(o.getCustomerName())
                .customerPhone(o.getCustomerPhone())
                .itemsJson(o.getItemsJson())
                .totalAmount(o.getTotalAmount())
                .diningIn(o.getDiningIn())
                .tableNumber(o.getTableNumber())
                .createdAt(o.getCreatedAt())
                .build()
        ).toList();
    }

    public OrderStatsResponse getStats() {

        BusinessEntity business = getOwnedBusinessOfCurrentUser();

        long totalOrders = orderRepository.countByBusiness(business);

        double totalRevenue = orderRepository.findTop20ByBusinessOrderByCreatedAtDesc(business).stream()
                .mapToDouble(OrderEntity::getTotalAmount)
                .sum();
        // Note: agar 20 se zyada orders ho jayen, total revenue ke liye alag query behtar hai (niche note dekho)

        LocalDateTime startOfToday = LocalDate.now().atStartOfDay();
        List<OrderEntity> todayOrders = orderRepository.findByBusinessAndCreatedAtAfter(business, startOfToday);

        double todayRevenue = todayOrders.stream().mapToDouble(OrderEntity::getTotalAmount).sum();

        long totalCustomers = customerRepository.countByBusiness(business);

        return OrderStatsResponse.builder()
                .totalOrders(totalOrders)
                .totalRevenue(totalRevenue)
                .todayOrders((long) todayOrders.size())
                .todayRevenue(todayRevenue)
                .totalCustomers(totalCustomers)
                .build();
    }

    private BusinessEntity getOwnedBusinessOfCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return businessRepository.findByOwnerId(user.getId())
                .orElseThrow(() -> new RuntimeException("Business not found"));
    }
}