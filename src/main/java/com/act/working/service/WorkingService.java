package com.act.working.service;

import com.act.working.dto.WorkingRequestDto;
import com.act.working.dto.WorkingUpdateDto;
import com.act.working.model.Working;

import java.util.List;

public interface WorkingService {
    Working get(Long id);

    List<Working> getAll(long id);

    List<Working> getAllByPositiveDone(long id);

    Working create(WorkingRequestDto workingRequestDto);

    Working update(long id, WorkingUpdateDto dto);

    void delete(long id);

    Working findWorkingOrNot(long id);
}
