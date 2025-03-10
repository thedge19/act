package com.act.working.service;

import com.act.exception.exception.NotFoundException;
import com.act.working.dto.WorkingMapper;
import com.act.working.dto.WorkingRequestDto;
import com.act.working.dto.WorkingResponseDto;
import com.act.working.model.Working;
import com.act.working.repository.WorkingRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class WorkingServiceImplementation implements WorkingService {

    private final WorkingRepository workingRepository;

    @Override
    public WorkingResponseDto get(Long id) {
        return WorkingMapper.INSTANCE.toDto(findWorkingOrNot(id));
    }

    @Transactional
    @Override
    public WorkingResponseDto create(WorkingRequestDto workingDto) {
        Working working = WorkingMapper.INSTANCE.toEntity(workingDto);
        return WorkingMapper.INSTANCE.toDto(workingRepository.save(working));
    }

    @Transactional
    @Override
    public WorkingResponseDto update(long id, WorkingRequestDto requestDto) {
        Working updatedWorking = findWorkingOrNot(id);
        log.info("Updating working with id: {}, name {}, standard {}", updatedWorking.getId(), updatedWorking.getName(), updatedWorking.getStandard().getName());

        if (requestDto.getName() != null) {
            updatedWorking.setName(requestDto.getName());
        }

        if (requestDto.getUnits() != null) {
            updatedWorking.setUnits(requestDto.getUnits());
        }

        if (requestDto.getQuantity() != null)  {
            updatedWorking.setQuantity(requestDto.getQuantity());
        }

        if (requestDto.getDone() != null) {
            updatedWorking.setDone(requestDto.getDone());
        }

        log.info("Updating working standard {}, units {}, quantity {}", updatedWorking.getStandard().getName(), updatedWorking.getUnits(), updatedWorking.getQuantity());

        return WorkingMapper.INSTANCE.toDto(workingRepository.save(updatedWorking));
    }

    @Transactional
    @Override
    public void delete(long id) {
        findWorkingOrNot(id);
        workingRepository.deleteById(id);
    }

    @Override
    public Working findWorkingOrNot(long id) {
        return workingRepository.findById(id).orElseThrow(() -> new NotFoundException("Подобъект не найден"));
    }
}
