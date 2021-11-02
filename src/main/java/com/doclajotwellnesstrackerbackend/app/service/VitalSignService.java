package com.doclajotwellnesstrackerbackend.app.service;

import com.doclajotwellnesstrackerbackend.app.service.dto.VitalSignDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.doclajotwellnesstrackerbackend.app.domain.VitalSign}.
 */
public interface VitalSignService {
    /**
     * Save a vitalSign.
     *
     * @param vitalSignDTO the entity to save.
     * @return the persisted entity.
     */
    VitalSignDTO save(VitalSignDTO vitalSignDTO);

    /**
     * Partially updates a vitalSign.
     *
     * @param vitalSignDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<VitalSignDTO> partialUpdate(VitalSignDTO vitalSignDTO);

    /**
     * Get all the vitalSigns.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<VitalSignDTO> findAll(Pageable pageable);

    /**
     * Get all the vitalSigns with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<VitalSignDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" vitalSign.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<VitalSignDTO> findOne(Long id);

    /**
     * Delete the "id" vitalSign.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
