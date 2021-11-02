package com.doclajotwellnesstrackerbackend.app.service;

import com.doclajotwellnesstrackerbackend.app.domain.*; // for static metamodels
import com.doclajotwellnesstrackerbackend.app.domain.VitalSign;
import com.doclajotwellnesstrackerbackend.app.repository.VitalSignRepository;
import com.doclajotwellnesstrackerbackend.app.service.criteria.VitalSignCriteria;
import com.doclajotwellnesstrackerbackend.app.service.dto.VitalSignDTO;
import com.doclajotwellnesstrackerbackend.app.service.mapper.VitalSignMapper;
import java.util.List;
import javax.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link VitalSign} entities in the database.
 * The main input is a {@link VitalSignCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link VitalSignDTO} or a {@link Page} of {@link VitalSignDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class VitalSignQueryService extends QueryService<VitalSign> {

    private final Logger log = LoggerFactory.getLogger(VitalSignQueryService.class);

    private final VitalSignRepository vitalSignRepository;

    private final VitalSignMapper vitalSignMapper;

    public VitalSignQueryService(VitalSignRepository vitalSignRepository, VitalSignMapper vitalSignMapper) {
        this.vitalSignRepository = vitalSignRepository;
        this.vitalSignMapper = vitalSignMapper;
    }

    /**
     * Return a {@link List} of {@link VitalSignDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<VitalSignDTO> findByCriteria(VitalSignCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<VitalSign> specification = createSpecification(criteria);
        return vitalSignMapper.toDto(vitalSignRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link VitalSignDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<VitalSignDTO> findByCriteria(VitalSignCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<VitalSign> specification = createSpecification(criteria);
        return vitalSignRepository.findAll(specification, page).map(vitalSignMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(VitalSignCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<VitalSign> specification = createSpecification(criteria);
        return vitalSignRepository.count(specification);
    }

    /**
     * Function to convert {@link VitalSignCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<VitalSign> createSpecification(VitalSignCriteria criteria) {
        Specification<VitalSign> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), VitalSign_.id));
            }
            if (criteria.getWeightInPounds() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getWeightInPounds(), VitalSign_.weightInPounds));
            }
            if (criteria.getHeightInInches() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getHeightInInches(), VitalSign_.heightInInches));
            }
            if (criteria.getBmi() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getBmi(), VitalSign_.bmi));
            }
            if (criteria.getGlassOfWater() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getGlassOfWater(), VitalSign_.glassOfWater));
            }
            if (criteria.getSystolic() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getSystolic(), VitalSign_.systolic));
            }
            if (criteria.getDiastolic() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDiastolic(), VitalSign_.diastolic));
            }
            if (criteria.getCurrentBloodSugar() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCurrentBloodSugar(), VitalSign_.currentBloodSugar));
            }
            if (criteria.getLipidProfile() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getLipidProfile(), VitalSign_.lipidProfile));
            }
            if (criteria.getAppUserId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getAppUserId(), root -> root.join(VitalSign_.appUser, JoinType.LEFT).get(AppUser_.id))
                    );
            }
            if (criteria.getHealthConcernId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getHealthConcernId(),
                            root -> root.join(VitalSign_.healthConcerns, JoinType.LEFT).get(HealthConcern_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
