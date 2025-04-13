package com.act.act.dto;

import com.act.act.model.EntranceControl;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface EntranceControlMapper {

    EntranceControlMapper INSTANCE = Mappers.getMapper(EntranceControlMapper.class);

    @Mapping(target = "date", source = "date", dateFormat = "dd.MM.yyyy")
    EntranceControlExportDto toDto(EntranceControl entranceControl);
}
