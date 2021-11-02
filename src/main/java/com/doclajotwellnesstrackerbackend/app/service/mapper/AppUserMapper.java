package com.doclajotwellnesstrackerbackend.app.service.mapper;

import com.doclajotwellnesstrackerbackend.app.domain.AppUser;
import com.doclajotwellnesstrackerbackend.app.service.dto.AppUserDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link AppUser} and its DTO {@link AppUserDTO}.
 */
@Mapper(componentModel = "spring", uses = { UserMapper.class })
public interface AppUserMapper extends EntityMapper<AppUserDTO, AppUser> {
    @Mapping(target = "user", source = "user", qualifiedByName = "id")
    AppUserDTO toDto(AppUser s);

    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    AppUserDTO toDtoId(AppUser appUser);
}
