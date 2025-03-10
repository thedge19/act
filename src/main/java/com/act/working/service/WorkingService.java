package com.act.working.service;

import com.act.working.dto.WorkingRequestDto;
import com.act.working.dto.WorkingResponseDto;
import com.act.working.model.Working;

public interface WorkingService {
    WorkingResponseDto get(Long id);

    WorkingResponseDto create(WorkingRequestDto workingRequestDto);

    WorkingResponseDto update(long id, WorkingRequestDto requestDto);

    void delete(long id);

    Working findWorkingOrNot(long id);
}
