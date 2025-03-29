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
    @Mapping(target="submittedDocuments", source = "submittedDocuments")
    @Mapping(target="inAccordWith", source = "inAccordWith")
    @Mapping(target="nextWorks", source = "nextWorks")
    ActResponseDto toDto(Act act);

    ActUpdateResponseDto toUpdateDto(Act act);
}
