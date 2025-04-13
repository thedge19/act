package com.act.registry.dto;

import com.act.registry.model.Registry;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface RegistryMapper {

    RegistryMapper INSTANCE = Mappers.getMapper(RegistryMapper.class);

    @Mapping(target = "documentDate", source = "documentDate", dateFormat = "dd.MM.yyyy")
    @Mapping(target = "rowNumber", source = "rowNumber")
    @Mapping(target = "listInOrder", source = "listInOrder")
    RegistryResponseDto toDto(Registry registry);
}
