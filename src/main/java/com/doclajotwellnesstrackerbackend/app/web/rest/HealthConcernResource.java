package com.doclajotwellnesstrackerbackend.app.web.rest;

import com.doclajotwellnesstrackerbackend.app.repository.HealthConcernRepository;
import com.doclajotwellnesstrackerbackend.app.service.HealthConcernQueryService;
import com.doclajotwellnesstrackerbackend.app.service.HealthConcernService;
import com.doclajotwellnesstrackerbackend.app.service.criteria.HealthConcernCriteria;
import com.doclajotwellnesstrackerbackend.app.service.dto.HealthConcernDTO;
import com.doclajotwellnesstrackerbackend.app.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.doclajotwellnesstrackerbackend.app.domain.HealthConcern}.
 */
@RestController
@RequestMapping("/api")
public class HealthConcernResource {

    private final Logger log = LoggerFactory.getLogger(HealthConcernResource.class);

    private static final String ENTITY_NAME = "healthConcern";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final HealthConcernService healthConcernService;

    private final HealthConcernRepository healthConcernRepository;

    private final HealthConcernQueryService healthConcernQueryService;

    public HealthConcernResource(
        HealthConcernService healthConcernService,
        HealthConcernRepository healthConcernRepository,
        HealthConcernQueryService healthConcernQueryService
    ) {
        this.healthConcernService = healthConcernService;
        this.healthConcernRepository = healthConcernRepository;
        this.healthConcernQueryService = healthConcernQueryService;
    }

    /**
     * {@code POST  /health-concerns} : Create a new healthConcern.
     *
     * @param healthConcernDTO the healthConcernDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new healthConcernDTO, or with status {@code 400 (Bad Request)} if the healthConcern has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/health-concerns")
    public ResponseEntity<HealthConcernDTO> createHealthConcern(@Valid @RequestBody HealthConcernDTO healthConcernDTO)
        throws URISyntaxException {
        log.debug("REST request to save HealthConcern : {}", healthConcernDTO);
        if (healthConcernDTO.getId() != null) {
            throw new BadRequestAlertException("A new healthConcern cannot already have an ID", ENTITY_NAME, "idexists");
        }
        HealthConcernDTO result = healthConcernService.save(healthConcernDTO);
        return ResponseEntity
            .created(new URI("/api/health-concerns/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /health-concerns/:id} : Updates an existing healthConcern.
     *
     * @param id the id of the healthConcernDTO to save.
     * @param healthConcernDTO the healthConcernDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated healthConcernDTO,
     * or with status {@code 400 (Bad Request)} if the healthConcernDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the healthConcernDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/health-concerns/{id}")
    public ResponseEntity<HealthConcernDTO> updateHealthConcern(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody HealthConcernDTO healthConcernDTO
    ) throws URISyntaxException {
        log.debug("REST request to update HealthConcern : {}, {}", id, healthConcernDTO);
        if (healthConcernDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, healthConcernDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!healthConcernRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        HealthConcernDTO result = healthConcernService.save(healthConcernDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, healthConcernDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /health-concerns/:id} : Partial updates given fields of an existing healthConcern, field will ignore if it is null
     *
     * @param id the id of the healthConcernDTO to save.
     * @param healthConcernDTO the healthConcernDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated healthConcernDTO,
     * or with status {@code 400 (Bad Request)} if the healthConcernDTO is not valid,
     * or with status {@code 404 (Not Found)} if the healthConcernDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the healthConcernDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/health-concerns/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<HealthConcernDTO> partialUpdateHealthConcern(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody HealthConcernDTO healthConcernDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update HealthConcern partially : {}, {}", id, healthConcernDTO);
        if (healthConcernDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, healthConcernDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!healthConcernRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<HealthConcernDTO> result = healthConcernService.partialUpdate(healthConcernDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, healthConcernDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /health-concerns} : get all the healthConcerns.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of healthConcerns in body.
     */
    @GetMapping("/health-concerns")
    public ResponseEntity<List<HealthConcernDTO>> getAllHealthConcerns(HealthConcernCriteria criteria, Pageable pageable) {
        log.debug("REST request to get HealthConcerns by criteria: {}", criteria);
        Page<HealthConcernDTO> page = healthConcernQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /health-concerns/count} : count all the healthConcerns.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/health-concerns/count")
    public ResponseEntity<Long> countHealthConcerns(HealthConcernCriteria criteria) {
        log.debug("REST request to count HealthConcerns by criteria: {}", criteria);
        return ResponseEntity.ok().body(healthConcernQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /health-concerns/:id} : get the "id" healthConcern.
     *
     * @param id the id of the healthConcernDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the healthConcernDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/health-concerns/{id}")
    public ResponseEntity<HealthConcernDTO> getHealthConcern(@PathVariable Long id) {
        log.debug("REST request to get HealthConcern : {}", id);
        Optional<HealthConcernDTO> healthConcernDTO = healthConcernService.findOne(id);
        return ResponseUtil.wrapOrNotFound(healthConcernDTO);
    }

    /**
     * {@code DELETE  /health-concerns/:id} : delete the "id" healthConcern.
     *
     * @param id the id of the healthConcernDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/health-concerns/{id}")
    public ResponseEntity<Void> deleteHealthConcern(@PathVariable Long id) {
        log.debug("REST request to delete HealthConcern : {}", id);
        healthConcernService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
