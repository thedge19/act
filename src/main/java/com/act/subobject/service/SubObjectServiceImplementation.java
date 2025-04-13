package com.act.subobject.service;

import com.act.exception.exception.NotFoundException;
import com.act.subobject.dto.SubObjectMapper;
import com.act.subobject.dto.SubObjectRequestDto;
import com.act.subobject.model.SubObject;
import com.act.subobject.repository.SubObjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SubObjectServiceImplementation implements SubObjectService {

    private final SubObjectRepository subObjectRepository;



    @Override
    public SubObject get(Long id) {
        return subObjectRepository.findById(id).orElse(null);
    }

    @Override
    public List<SubObject> getAllByProjectId(long id) {
        return subObjectRepository.findAllByProjectIdOrderByIdAsc(id);
    }

    @Override
    public List<SubObject> getAll() {
        return subObjectRepository.findAll();
    }


    @Transactional
    @Override
    public SubObject create(SubObjectRequestDto dto) {
        return subObjectRepository.save(SubObjectMapper.INSTANCE.toEntity(dto));
    }

    @Transactional
    @Override
    public SubObject update(long id, SubObject subObject) {
        SubObject updatedSubObject = findSubObjectOrNot(id);

        if (subObject.getName() != null) {
            updatedSubObject.setName(subObject.getName());
        }

        if (subObject.getTitle() != null) {
            updatedSubObject.setTitle(subObject.getTitle());
        }

        return subObjectRepository.save(updatedSubObject);
    }

    @Transactional
    @Override
    public void delete(long id) {
        findSubObjectOrNot(id);
        subObjectRepository.deleteById(id);
    }

    @Override
    public SubObject findSubObjectOrNot(long id) {
        return subObjectRepository.findById(id).orElseThrow(() -> new NotFoundException("Подобъект не найден"));
    }
}
