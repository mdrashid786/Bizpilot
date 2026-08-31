package com.bizpilot.business.entity;

import com.bizpilot.business.model.OrderStatus;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "customer_order")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "business_id", nullable = false)
    private BusinessEntity business;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "customer_id", nullable = false)
    private CustomerEntity customer;

    @Column(name = "customer_name", nullable = false, length = 120)
    private String customerName;

    @Column(name = "customer_phone", nullable = false, length = 20)
    private String customerPhone;

    @Column(name = "items_json", columnDefinition = "TEXT", nullable = false)
    private String itemsJson; // [{"name":"Paneer Tikka","price":280,"qty":2}, ...]

    @Column(name = "total_amount", nullable = false)
    private Double totalAmount;

    @Column(name = "dining_in")
    private Boolean diningIn;

    @Column(name = "table_number", length = 50)
    private String tableNumber;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private OrderStatus status = OrderStatus.PENDING;
}