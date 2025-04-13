package com.act.worklog.dto;

import com.act.worklog.model.WorkLog;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface WorkLogMapper {

    WorkLogMapper INSTANCE = Mappers.getMapper(WorkLogMapper.class);

    @Mapping(target = "workDate", source = "workDate", dateFormat = "dd.MM.yyyy")
    WorkLogDto toDto(WorkLog workLog);
}
