package com.bizpilot.business.repository;

import com.bizpilot.business.entity.BusinessEntity;
import com.bizpilot.business.entity.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface OrderRepository extends JpaRepository<OrderEntity, Long> {

    List<OrderEntity> findTop20ByBusinessOrderByCreatedAtDesc(BusinessEntity business);

    long countByBusiness(BusinessEntity business);

    List<OrderEntity> findByBusinessAndCreatedAtAfter(BusinessEntity business, LocalDateTime after);
    // OrderRepository mein add karo:
    @Query("SELECT COALESCE(SUM(o.totalAmount), 0) FROM OrderEntity o WHERE o.business = :business")
    Double sumTotalAmountByBusiness(BusinessEntity business);
}