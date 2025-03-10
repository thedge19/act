package com.act.material.service;

import com.act.material.model.Material;

public interface MaterialService {
    Material get(Long id);

    Material create(Material material);

    Material update(long id, Material material);

    void delete(long id);

    Material findMaterialOrNot(long id);
}
