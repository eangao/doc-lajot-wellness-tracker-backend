package com.doclajotwellnesstrackerbackend.app.service.mapper;

import com.doclajotwellnesstrackerbackend.app.domain.HealthConcern;
import com.doclajotwellnesstrackerbackend.app.service.dto.HealthConcernDTO;
import java.util.Set;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link HealthConcern} and its DTO {@link HealthConcernDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface HealthConcernMapper extends EntityMapper<HealthConcernDTO, HealthConcern> {
    @Named("nameSet")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    Set<HealthConcernDTO> toDtoNameSet(Set<HealthConcern> healthConcern);
}
