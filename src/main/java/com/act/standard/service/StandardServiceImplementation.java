package com.act.standard.service;

import com.act.exception.exception.NotFoundException;
import com.act.standard.model.Standard;
import com.act.standard.repository.StandardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StandardServiceImplementation implements StandardService {

    private final StandardRepository standardRepository;

    @Override
    public Standard get(Long id) {
        return standardRepository.findById(id).orElse(null);
    }

    @Transactional
    @Override
    public Standard create(Standard standard) {
        return standardRepository.save(standard);
    }

    @Transactional
    @Override
    public Standard update(long id, Standard standard) {
        Standard updatedStandard = findStandardOrNot(id);

        if (standard.getName() != null) {
            updatedStandard.setName(standard.getName());
        }

        return standardRepository.save(updatedStandard);
    }

    @Transactional
    @Override
    public void delete(long id) {
        findStandardOrNot(id);
        standardRepository.deleteById(id);
    }

    @Override
    public Standard findStandardOrNot(long id) {
        return standardRepository.findById(id).orElseThrow(() -> new NotFoundException("Подобъект не найден"));
    }
}