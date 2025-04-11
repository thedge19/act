package com.act.registry.service;

import com.act.registry.dto.*;
import com.act.registry.model.Registry;
import com.itextpdf.text.DocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import java.io.IOException;
import java.util.List;

public interface RegistryService {
    Registry findRegistryOrNot(Long id);

    List<RegistryResponseDto> getAllByMonth(int monthId);

    void create(RegistryDto dto);

    void update(List<RegistryUpdateRequestDto> dtos);

    void updateNumberOfPages(long id, int numberOfSheets);

    void delete(Long id);

    void excelExport(int monthId) throws IOException, DocumentException, InvalidFormatException;
}
