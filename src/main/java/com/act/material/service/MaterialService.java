package com.act.material.service;

import com.act.material.model.Material;

import java.util.List;

public interface MaterialService {
    Material get(Long id);

    List<Material> getAll();

    Material create(Material material);

    Material update(long id, Material material);

    void delete(long id);

    Material findMaterialOrNot(long id);
}
