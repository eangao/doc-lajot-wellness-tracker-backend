package com.doclajotwellnesstrackerbackend.app.repository;

import com.doclajotwellnesstrackerbackend.app.domain.HealthConcern;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the HealthConcern entity.
 */
@SuppressWarnings("unused")
@Repository
public interface HealthConcernRepository extends JpaRepository<HealthConcern, Long>, JpaSpecificationExecutor<HealthConcern> {}
