package com.act.act.service;

import com.act.act.dto.*;
import com.act.act.model.Act;
import com.act.act.model.EntranceControl;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;

public interface ActService {
    ActResponseDto get(Long id);

    ActUpdateResponseDto getUpdatedAct(long id);

    List<ActResponseDto> getAll();

    List<ActResponseDto> findAllByEndDateBetween(LocalDate startDate, LocalDate endDate);

    List<EntranceControlResponseDto> getAllEntranceControl();

    Act create(ActRequestDto requestDto);

    Act update(long id, ActUpdateRequestDto requestDto);

    void delete(long id);

    void deleteControl(long id);

    Act findActOrNot(long id);

    EntranceControl findEntranceControl(long id);

    List<EntranceControl> controls(Act act);

    EntranceControl updateEntranceControl(long id, EntranceControlRequestDto requestDto);

    LocalDate jsDateToLocalDate(String date);

    List<ActResponseDto> filterBySubObject();

    List<ActResponseDto> getAllWithNullInRegistries();

    void addSchema(MultipartFile file, Long id);
}
