package com.doclajotwellnesstrackerbackend.app.service.mapper;

import com.doclajotwellnesstrackerbackend.app.domain.VitalSign;
import com.doclajotwellnesstrackerbackend.app.service.dto.VitalSignDTO;
import java.util.Set;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link VitalSign} and its DTO {@link VitalSignDTO}.
 */
@Mapper(componentModel = "spring", uses = { AppUserMapper.class, HealthConcernMapper.class })
public interface VitalSignMapper extends EntityMapper<VitalSignDTO, VitalSign> {
    @Mapping(target = "appUser", source = "appUser", qualifiedByName = "id")
    @Mapping(target = "healthConcerns", source = "healthConcerns", qualifiedByName = "nameSet")
    VitalSignDTO toDto(VitalSign s);

    @Mapping(target = "removeHealthConcern", ignore = true)
    VitalSign toEntity(VitalSignDTO vitalSignDTO);
}
