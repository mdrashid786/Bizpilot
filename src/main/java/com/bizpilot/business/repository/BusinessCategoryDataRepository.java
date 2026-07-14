package com.bizpilot.business.repository;

import com.bizpilot.business.entity.BusinessCategoryDataEntity;
import com.bizpilot.business.entity.BusinessEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BusinessCategoryDataRepository
        extends JpaRepository<BusinessCategoryDataEntity, Long> {

    List<BusinessCategoryDataEntity> findByBusinessOrderBySortOrder(
            BusinessEntity business
    );
}