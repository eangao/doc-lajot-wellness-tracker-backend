package com.doclajotwellnesstrackerbackend.app.service;

import com.doclajotwellnesstrackerbackend.app.service.dto.HealthConcernDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.doclajotwellnesstrackerbackend.app.domain.HealthConcern}.
 */
public interface HealthConcernService {
    /**
     * Save a healthConcern.
     *
     * @param healthConcernDTO the entity to save.
     * @return the persisted entity.
     */
    HealthConcernDTO save(HealthConcernDTO healthConcernDTO);

    /**
     * Partially updates a healthConcern.
     *
     * @param healthConcernDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<HealthConcernDTO> partialUpdate(HealthConcernDTO healthConcernDTO);

    /**
     * Get all the healthConcerns.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<HealthConcernDTO> findAll(Pageable pageable);

    /**
     * Get the "id" healthConcern.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<HealthConcernDTO> findOne(Long id);

    /**
     * Delete the "id" healthConcern.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
