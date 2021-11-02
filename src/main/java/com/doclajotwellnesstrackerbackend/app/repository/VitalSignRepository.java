package com.doclajotwellnesstrackerbackend.app.repository;

import com.doclajotwellnesstrackerbackend.app.domain.VitalSign;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the VitalSign entity.
 */
@Repository
public interface VitalSignRepository extends JpaRepository<VitalSign, Long>, JpaSpecificationExecutor<VitalSign> {
    @Query(
        value = "select distinct vitalSign from VitalSign vitalSign left join fetch vitalSign.healthConcerns",
        countQuery = "select count(distinct vitalSign) from VitalSign vitalSign"
    )
    Page<VitalSign> findAllWithEagerRelationships(Pageable pageable);

    @Query("select distinct vitalSign from VitalSign vitalSign left join fetch vitalSign.healthConcerns")
    List<VitalSign> findAllWithEagerRelationships();

    @Query("select vitalSign from VitalSign vitalSign left join fetch vitalSign.healthConcerns where vitalSign.id =:id")
    Optional<VitalSign> findOneWithEagerRelationships(@Param("id") Long id);
}
