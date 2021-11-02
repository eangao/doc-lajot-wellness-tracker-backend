package com.doclajotwellnesstrackerbackend.app.service.impl;

import com.doclajotwellnesstrackerbackend.app.domain.HealthConcern;
import com.doclajotwellnesstrackerbackend.app.repository.HealthConcernRepository;
import com.doclajotwellnesstrackerbackend.app.service.HealthConcernService;
import com.doclajotwellnesstrackerbackend.app.service.dto.HealthConcernDTO;
import com.doclajotwellnesstrackerbackend.app.service.mapper.HealthConcernMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link HealthConcern}.
 */
@Service
@Transactional
public class HealthConcernServiceImpl implements HealthConcernService {

    private final Logger log = LoggerFactory.getLogger(HealthConcernServiceImpl.class);

    private final HealthConcernRepository healthConcernRepository;

    private final HealthConcernMapper healthConcernMapper;

    public HealthConcernServiceImpl(HealthConcernRepository healthConcernRepository, HealthConcernMapper healthConcernMapper) {
        this.healthConcernRepository = healthConcernRepository;
        this.healthConcernMapper = healthConcernMapper;
    }

    @Override
    public HealthConcernDTO save(HealthConcernDTO healthConcernDTO) {
        log.debug("Request to save HealthConcern : {}", healthConcernDTO);
        HealthConcern healthConcern = healthConcernMapper.toEntity(healthConcernDTO);
        healthConcern = healthConcernRepository.save(healthConcern);
        return healthConcernMapper.toDto(healthConcern);
    }

    @Override
    public Optional<HealthConcernDTO> partialUpdate(HealthConcernDTO healthConcernDTO) {
        log.debug("Request to partially update HealthConcern : {}", healthConcernDTO);

        return healthConcernRepository
            .findById(healthConcernDTO.getId())
            .map(existingHealthConcern -> {
                healthConcernMapper.partialUpdate(existingHealthConcern, healthConcernDTO);

                return existingHealthConcern;
            })
            .map(healthConcernRepository::save)
            .map(healthConcernMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<HealthConcernDTO> findAll(Pageable pageable) {
        log.debug("Request to get all HealthConcerns");
        return healthConcernRepository.findAll(pageable).map(healthConcernMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<HealthConcernDTO> findOne(Long id) {
        log.debug("Request to get HealthConcern : {}", id);
        return healthConcernRepository.findById(id).map(healthConcernMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete HealthConcern : {}", id);
        healthConcernRepository.deleteById(id);
    }
}
