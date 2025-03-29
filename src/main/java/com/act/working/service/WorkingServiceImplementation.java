package com.act.working.service;

import com.act.exception.exception.NotFoundException;
import com.act.working.dto.WorkingMapper;
import com.act.working.dto.WorkingRequestDto;
import com.act.working.dto.WorkingUpdateDto;
import com.act.working.model.Working;
import com.act.working.repository.WorkingRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class WorkingServiceImplementation implements WorkingService {

    private final WorkingRepository workingRepository;

    @Override
    public Working get(Long id) {
        return findWorkingOrNot(id);
    }

    @Override
    public List<Working> getAll(long id) {
        return workingRepository.findAllBySubObjectIdOrderByIdAsc(id);
    }

    @Transactional
    @Override
    public Working create(WorkingRequestDto workingDto) {
        return workingRepository.save(WorkingMapper.INSTANCE.toEntity(workingDto));
    }

    @Transactional
    @Override
    public Working update(long id, WorkingUpdateDto dto) {
        Working updatedWorking = findWorkingOrNot(id);
        log.info("Updating working with id: {}, name {}, standard {}", updatedWorking.getId(), updatedWorking.getName(), updatedWorking.getStandard().getName());

        if (dto.getName() != null) {
            updatedWorking.setName(dto.getName());
        }

        if (dto.getUnits() != null) {
            updatedWorking.setUnits(dto.getUnits());
        }

        if (dto.getQuantity() != null)  {
            updatedWorking.setQuantity(dto.getQuantity());
        }

        if (dto.getDone() != null) {
            updatedWorking.setDone(dto.getDone());
        }

        log.info("Updating working standard {}, units {}, quantity {}", updatedWorking.getStandard().getName(), updatedWorking.getUnits(), updatedWorking.getQuantity());

        return workingRepository.save(updatedWorking);
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
