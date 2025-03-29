package com.act.act.service;

import com.act.act.dto.ActRequestDto;
import com.act.act.dto.ActResponseDto;
import com.act.act.dto.ActUpdateRequestDto;
import com.act.act.dto.ActUpdateResponseDto;
import com.act.act.model.Act;
import com.act.act.model.EntranceControl;

import java.time.LocalDate;
import java.util.List;

public interface ActService {
    ActResponseDto get(Long id);

    ActUpdateResponseDto getUpdatedAct(long id);

    List<ActResponseDto> getAll();

    List<ActResponseDto> findAllByEndDateBetween(LocalDate startDate, LocalDate endDate);

    List<EntranceControl> getAllEntranceControl();

    Act create(ActRequestDto requestDto);

    Act update(long id, ActUpdateRequestDto requestDto);

    void delete(long id);

    void deleteControl(long id);

    Act findActOrNot(long id);

    List<EntranceControl> controls(Act act);

    LocalDate jsDateToLocalDate(String date);
}
