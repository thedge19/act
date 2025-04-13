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
    @Mapping(target="inRegistry", source = "inRegistry")
    ActResponseDto toDto(Act act);

    ActUpdateResponseDto toUpdateDto(Act act);

    @Mapping(target="actNumber", source = "actNumber")
    @Mapping(target="works", source = "works")
    @Mapping(target = "endDate", source = "endDate", dateFormat = "dd.MM.yyyy")
    ActLogResponseDto toLogDto(Act act);
}
