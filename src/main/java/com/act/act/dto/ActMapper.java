package com.act.act.dto;

import com.act.act.model.Act;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ActMapper {

    ActMapper INSTANCE = Mappers.getMapper(ActMapper.class);

    @Mapping(target = "projectName", source = "project.name")
    @Mapping(target = "startDate", source = "startDate", dateFormat = "dd-MM-yyyy")
    @Mapping(target = "endDate", source = "endDate", dateFormat = "dd-MM-yyyy")
    @Mapping(target="materials", source = "materials")
    ActResponseDto toDto(Act act);

//    @Mapping(target = "subObject.id", source = "subObjectId")
//    @Mapping(target = "project.id", source = "projectId")
//    Act toEntity(ActRequestDto requestDto);
}
