package com.bizpilot.business.repository;

import com.bizpilot.business.entity.BusinessEntity;
import com.bizpilot.business.entity.CustomerEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CustomerRepository extends JpaRepository<CustomerEntity, Long> {
    Optional<CustomerEntity> findByBusinessAndPhone(BusinessEntity business, String phone);
    long countByBusiness(BusinessEntity business);
}