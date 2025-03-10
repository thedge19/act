package com.act.working.dto;

import com.act.working.model.Working;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface WorkingMapper {

    WorkingMapper INSTANCE = Mappers.getMapper(WorkingMapper.class);

    @Mapping(target = "subObjectId", source = "subObject.id")
    @Mapping(target = "standardId", source = "standard.id")
    @Mapping(target = "finalQuantity", source = "finalQuantity")
    WorkingResponseDto toDto(Working working);

    @Mapping(target = "subObject.id", source = "subObjectId")
    @Mapping(target = "standard.id", source = "standardId")
    Working toEntity(WorkingRequestDto workingDto);
}
