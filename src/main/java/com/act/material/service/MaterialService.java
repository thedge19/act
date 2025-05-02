package com.act.material.service;

import com.act.material.dto.MaterialResponseDto;
import com.act.material.model.Material;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface MaterialService {
    MaterialResponseDto get(Long id);

    List<MaterialResponseDto> getAll();

    Material create(Material material);

    Material update(long id, Material material);

    void delete(long id);

    Material findMaterialOrNot(long id);

    void addCertificate(long id, MultipartFile file);
}
