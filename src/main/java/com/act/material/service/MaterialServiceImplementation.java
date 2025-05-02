package com.act.material.service;

import com.act.exception.exception.NotFoundException;
import com.act.material.dto.MaterialMapper;
import com.act.material.dto.MaterialResponseDto;
import com.act.material.model.Certificate;
import com.act.material.model.Material;
import com.act.material.repository.CertificateRepository;
import com.act.material.repository.MaterialRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileOutputStream;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MaterialServiceImplementation implements MaterialService {

    private final MaterialRepository materialRepository;
    private final CertificateRepository certificateRepository;

    @Override
    public MaterialResponseDto get(Long id) {
        return MaterialMapper.INSTANCE.toResponseDto(findMaterialOrNot(id));
    }

    @Override
    public List<MaterialResponseDto> getAll() {

        return materialRepository
                .findAllByOrderByName()
                .stream()
                .map(MaterialMapper.INSTANCE::toResponseDto)
                .collect(Collectors.toList());
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

    @Transactional
    @Override
    public void addCertificate(long id, MultipartFile file) {
        Material material = findMaterialOrNot(id);
        Certificate certificate = new Certificate();
        certificate.setMaterial(material);

        String PATH_FOLDER = "C:\\Users\\PC\\IdeaProjects\\AOSR\\AOSR\\act\\certificates\\";
        String path = PATH_FOLDER + material.getId() + ".pdf";
        certificate.setPath(path);

        try {
            // Creating an object of FileOutputStream class
            FileOutputStream fos = new FileOutputStream(path);
            fos.write(file.getBytes());

            // Closing the connection
            fos.close();
        } catch (Exception e) {
            log.info(e.getMessage());
        }

        certificateRepository.save(certificate);

        log.info("Номер загруженного сертификата: {}", certificate.getId());

        material.setCertificate(certificate);
    }
}