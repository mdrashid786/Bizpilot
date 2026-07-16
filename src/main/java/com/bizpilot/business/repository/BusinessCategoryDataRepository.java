package com.bizpilot.business.repository;

import com.bizpilot.business.entity.BusinessCategoryDataEntity;
import com.bizpilot.business.entity.BusinessEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface BusinessCategoryDataRepository
        extends JpaRepository<BusinessCategoryDataEntity, Long> {

    List<BusinessCategoryDataEntity> findByBusinessOrderBySortOrder(
            BusinessEntity business
    );

    Integer countByBusiness(BusinessEntity business);

    List<BusinessCategoryDataEntity> findByBusinessAndSortOrder(
            BusinessEntity business,
            Integer sortOrder);

    void deleteByBusinessAndSortOrder(
            BusinessEntity business,
            Integer sortOrder);

    List<BusinessCategoryDataEntity> findByBusinessIdOrderBySortOrder(Long businessId);

    @Query("""
       SELECT COALESCE(MAX(b.sortOrder), 0)
       FROM BusinessCategoryDataEntity b
       WHERE b.business.id = :businessId
       """)
    Integer findMaxSortOrder(Long businessId);

    List<BusinessCategoryDataEntity> findByBusinessIdAndSortOrder(
            Long businessId,
            Integer sortOrder);

    List<BusinessCategoryDataEntity> findByBusinessIdOrderBySortOrderAscIdAsc(Long businessId);

    List<BusinessCategoryDataEntity> findByBusinessIdAndRowId(Long businessId, String rowId);

    void deleteByBusinessIdAndRowId(Long businessId, String rowId);

    Optional<BusinessCategoryDataEntity> findFirstByBusinessIdOrderBySortOrderDesc(Long businessId);
}