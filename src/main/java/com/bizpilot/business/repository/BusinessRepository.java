package com.bizpilot.business.repository;

import com.bizpilot.authentication.entity.UserEntity;
import com.bizpilot.business.entity.BusinessEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BusinessRepository extends JpaRepository<BusinessEntity, Long> {

    Optional<BusinessEntity> findBySlug(String slug);

    Optional<BusinessEntity> findBySlugAndPublishedTrue(String slug);

    boolean existsBySlug(String slug);

    Optional<BusinessEntity>  findByOwner(UserEntity owner);
}
