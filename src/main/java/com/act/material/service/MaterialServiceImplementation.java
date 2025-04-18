package com.act.material.service;

import com.act.exception.exception.NotFoundException;
import com.act.material.model.Material;
import com.act.material.repository.MaterialRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MaterialServiceImplementation implements MaterialService {

    private final MaterialRepository materialRepository;

    @Override
    public Material get(Long id) {
        return findMaterialOrNot(id);
    }

    @Override
    public List<Material> getAll() {

        return materialRepository.findAllByOrderByName();
    }

    @Transactional
    @Override
    public Material create(Material material) {
        return materialRepository.save(material);
    }

    @Transactional
    @Override
    public Material update(long id, Material material) {
        Material updatedMaterial = findMaterialOrNot(id);

        if (material.getName() != null) {
            updatedMaterial.setName(material.getName());
        }

        return updatedMaterial;
    }

    @Transactional
    @Override
    public void delete(long id) {
        findMaterialOrNot(id);
        materialRepository.deleteById(id);
    }

    @Override
    public Material findMaterialOrNot(long id) {
        return materialRepository.findById(id).orElseThrow(() -> new NotFoundException("Подобъект не найден"));
    }
}