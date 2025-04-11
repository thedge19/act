package com.act.subobject.dto;

import com.act.subobject.model.SubObject;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface SubObjectMapper {

    SubObjectMapper INSTANCE = Mappers.getMapper(SubObjectMapper.class);

    @Mapping(target = "project.id", source = "projectId")
    SubObject toEntity(SubObjectRequestDto dto);
}
