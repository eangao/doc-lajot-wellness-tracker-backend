package com.doclajotwellnesstrackerbackend.app.web.rest;

import com.doclajotwellnesstrackerbackend.app.repository.VitalSignRepository;
import com.doclajotwellnesstrackerbackend.app.service.VitalSignQueryService;
import com.doclajotwellnesstrackerbackend.app.service.VitalSignService;
import com.doclajotwellnesstrackerbackend.app.service.criteria.VitalSignCriteria;
import com.doclajotwellnesstrackerbackend.app.service.dto.VitalSignDTO;
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
 * REST controller for managing {@link com.doclajotwellnesstrackerbackend.app.domain.VitalSign}.
 */
@RestController
@RequestMapping("/api")
public class VitalSignResource {

    private final Logger log = LoggerFactory.getLogger(VitalSignResource.class);

    private static final String ENTITY_NAME = "vitalSign";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final VitalSignService vitalSignService;

    private final VitalSignRepository vitalSignRepository;

    private final VitalSignQueryService vitalSignQueryService;

    public VitalSignResource(
        VitalSignService vitalSignService,
        VitalSignRepository vitalSignRepository,
        VitalSignQueryService vitalSignQueryService
    ) {
        this.vitalSignService = vitalSignService;
        this.vitalSignRepository = vitalSignRepository;
        this.vitalSignQueryService = vitalSignQueryService;
    }

    /**
     * {@code POST  /vital-signs} : Create a new vitalSign.
     *
     * @param vitalSignDTO the vitalSignDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new vitalSignDTO, or with status {@code 400 (Bad Request)} if the vitalSign has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/vital-signs")
    public ResponseEntity<VitalSignDTO> createVitalSign(@Valid @RequestBody VitalSignDTO vitalSignDTO) throws URISyntaxException {
        log.debug("REST request to save VitalSign : {}", vitalSignDTO);
        if (vitalSignDTO.getId() != null) {
            throw new BadRequestAlertException("A new vitalSign cannot already have an ID", ENTITY_NAME, "idexists");
        }
        VitalSignDTO result = vitalSignService.save(vitalSignDTO);
        return ResponseEntity
            .created(new URI("/api/vital-signs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /vital-signs/:id} : Updates an existing vitalSign.
     *
     * @param id the id of the vitalSignDTO to save.
     * @param vitalSignDTO the vitalSignDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated vitalSignDTO,
     * or with status {@code 400 (Bad Request)} if the vitalSignDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the vitalSignDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/vital-signs/{id}")
    public ResponseEntity<VitalSignDTO> updateVitalSign(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody VitalSignDTO vitalSignDTO
    ) throws URISyntaxException {
        log.debug("REST request to update VitalSign : {}, {}", id, vitalSignDTO);
        if (vitalSignDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, vitalSignDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!vitalSignRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        VitalSignDTO result = vitalSignService.save(vitalSignDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, vitalSignDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /vital-signs/:id} : Partial updates given fields of an existing vitalSign, field will ignore if it is null
     *
     * @param id the id of the vitalSignDTO to save.
     * @param vitalSignDTO the vitalSignDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated vitalSignDTO,
     * or with status {@code 400 (Bad Request)} if the vitalSignDTO is not valid,
     * or with status {@code 404 (Not Found)} if the vitalSignDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the vitalSignDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/vital-signs/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<VitalSignDTO> partialUpdateVitalSign(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody VitalSignDTO vitalSignDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update VitalSign partially : {}, {}", id, vitalSignDTO);
        if (vitalSignDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, vitalSignDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!vitalSignRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<VitalSignDTO> result = vitalSignService.partialUpdate(vitalSignDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, vitalSignDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /vital-signs} : get all the vitalSigns.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of vitalSigns in body.
     */
    @GetMapping("/vital-signs")
    public ResponseEntity<List<VitalSignDTO>> getAllVitalSigns(VitalSignCriteria criteria, Pageable pageable) {
        log.debug("REST request to get VitalSigns by criteria: {}", criteria);
        Page<VitalSignDTO> page = vitalSignQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /vital-signs/count} : count all the vitalSigns.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/vital-signs/count")
    public ResponseEntity<Long> countVitalSigns(VitalSignCriteria criteria) {
        log.debug("REST request to count VitalSigns by criteria: {}", criteria);
        return ResponseEntity.ok().body(vitalSignQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /vital-signs/:id} : get the "id" vitalSign.
     *
     * @param id the id of the vitalSignDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the vitalSignDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/vital-signs/{id}")
    public ResponseEntity<VitalSignDTO> getVitalSign(@PathVariable Long id) {
        log.debug("REST request to get VitalSign : {}", id);
        Optional<VitalSignDTO> vitalSignDTO = vitalSignService.findOne(id);
        return ResponseUtil.wrapOrNotFound(vitalSignDTO);
    }

    /**
     * {@code DELETE  /vital-signs/:id} : delete the "id" vitalSign.
     *
     * @param id the id of the vitalSignDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/vital-signs/{id}")
    public ResponseEntity<Void> deleteVitalSign(@PathVariable Long id) {
        log.debug("REST request to delete VitalSign : {}", id);
        vitalSignService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
