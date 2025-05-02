package com.act.material.dto;

import com.act.material.model.Material;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface MaterialMapper {

    MaterialMapper INSTANCE = Mappers.getMapper(MaterialMapper.class);

    @Mapping(target = "certificateId", source = "certificate.id")
    MaterialResponseDto toResponseDto(Material material);

}
