package com.doclajotwellnesstrackerbackend.app.service.impl;

import com.doclajotwellnesstrackerbackend.app.domain.VitalSign;
import com.doclajotwellnesstrackerbackend.app.repository.VitalSignRepository;
import com.doclajotwellnesstrackerbackend.app.service.VitalSignService;
import com.doclajotwellnesstrackerbackend.app.service.dto.VitalSignDTO;
import com.doclajotwellnesstrackerbackend.app.service.mapper.VitalSignMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link VitalSign}.
 */
@Service
@Transactional
public class VitalSignServiceImpl implements VitalSignService {

    private final Logger log = LoggerFactory.getLogger(VitalSignServiceImpl.class);

    private final VitalSignRepository vitalSignRepository;

    private final VitalSignMapper vitalSignMapper;

    public VitalSignServiceImpl(VitalSignRepository vitalSignRepository, VitalSignMapper vitalSignMapper) {
        this.vitalSignRepository = vitalSignRepository;
        this.vitalSignMapper = vitalSignMapper;
    }

    @Override
    public VitalSignDTO save(VitalSignDTO vitalSignDTO) {
        log.debug("Request to save VitalSign : {}", vitalSignDTO);
        VitalSign vitalSign = vitalSignMapper.toEntity(vitalSignDTO);
        vitalSign = vitalSignRepository.save(vitalSign);
        return vitalSignMapper.toDto(vitalSign);
    }

    @Override
    public Optional<VitalSignDTO> partialUpdate(VitalSignDTO vitalSignDTO) {
        log.debug("Request to partially update VitalSign : {}", vitalSignDTO);

        return vitalSignRepository
            .findById(vitalSignDTO.getId())
            .map(existingVitalSign -> {
                vitalSignMapper.partialUpdate(existingVitalSign, vitalSignDTO);

                return existingVitalSign;
            })
            .map(vitalSignRepository::save)
            .map(vitalSignMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<VitalSignDTO> findAll(Pageable pageable) {
        log.debug("Request to get all VitalSigns");
        return vitalSignRepository.findAll(pageable).map(vitalSignMapper::toDto);
    }

    public Page<VitalSignDTO> findAllWithEagerRelationships(Pageable pageable) {
        return vitalSignRepository.findAllWithEagerRelationships(pageable).map(vitalSignMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<VitalSignDTO> findOne(Long id) {
        log.debug("Request to get VitalSign : {}", id);
        return vitalSignRepository.findOneWithEagerRelationships(id).map(vitalSignMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete VitalSign : {}", id);
        vitalSignRepository.deleteById(id);
    }
}
