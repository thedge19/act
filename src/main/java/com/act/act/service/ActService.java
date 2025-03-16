package com.act.act.service;

import com.act.act.dto.ActRequestDto;
import com.act.act.dto.ActResponseDto;
import com.act.act.model.Act;
import com.act.act.model.EntranceControl;

import java.util.List;

public interface ActService {
    ActResponseDto get(Long id);

    List<ActResponseDto> getAll();

    List<EntranceControl> getAllEntranceControl();

    Act create(ActRequestDto requestDto);

    Act update(long id, ActRequestDto requestDto);

    void delete(long id);

    void deleteControl(long id);

    Act findActOrNot(long id);
}
