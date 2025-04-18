package com.act.main.service;

import com.act.pdf.service.PdfService;
import com.act.registry.model.Registry;
import com.act.registry.repository.RegistryRepository;
import com.act.registry.service.RegistryService;
import com.itextpdf.text.DocumentException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MainServiceImplementation implements MainService {

    private final RegistryService registryService;
    private final RegistryRepository registryRepository;
    private final static String ENERGY = "ООО Энергомонтаж";
    private final PdfService pdfService;
    public static final String REGISTRY_PATH = "C:\\Users\\PC\\Desktop\\work\\registry.pdf";
    public static final String REGISTRY_TEMP_PATH = "C:\\Users\\PC\\Desktop\\work\\temp_registry.pdf";

    @Transactional
    @Override
    public void createRegistry(int id) throws IOException, DocumentException {

        if (registryRepository.findByMonthId(id, "Общий журнал работ") == null) {
            Registry workLogregistry = new Registry();
            workLogregistry.setDocumentName("Общий журнал работ");
            workLogregistry.setDocumentNumber("б/н");
            workLogregistry.setDocumentAuthor(ENERGY);
            workLogregistry.setDocumentDate(LocalDate.of(2024, 9, 2));
            workLogregistry.setMonthId(id);

            pdfService.exportWorkLog3ToPdf();
            pdfService.exportWorkLog6ToPdf();
            pdfService.mergeUsingIText(1);

            workLogregistry.setNumberOfSheets(pdfService.numberOfPages("workLog"));
            workLogregistry.setRowNumber(registryRepository.countByMonthId(id) + 1);
            workLogregistry.setAddingTime(LocalDateTime.now());

            registryRepository.save(workLogregistry);
        }

        if (registryRepository.findByMonthId(id, "Журнал входного контроля") == null) {
            Registry entranceControlRegistry = new Registry();

            entranceControlRegistry.setDocumentName("Журнал входного контроля");
            entranceControlRegistry.setDocumentNumber("б/н");
            entranceControlRegistry.setDocumentAuthor(ENERGY);
            entranceControlRegistry.setDocumentDate(LocalDate.of(2024, 9, 2));
            entranceControlRegistry.setMonthId(id);

            pdfService.exportEntranceControlLogToPdf();

            entranceControlRegistry.setNumberOfSheets(pdfService.numberOfPages("entranceControlLog"));
            entranceControlRegistry.setRowNumber(registryRepository.countByMonthId(id) + 1);
            entranceControlRegistry.setAddingTime(LocalDateTime.now());

            registryRepository.save(entranceControlRegistry);
        }

        pdfService.exportRegistryToPdf(id, REGISTRY_TEMP_PATH);

        int registryNumberOfPages = pdfService.numberOfPages("registryTemp");
        log.info("{}", registryNumberOfPages);

        Registry registry = registryRepository.findByMonthId(id, "Реестр исполнительной документации");
        registry.setNumberOfSheets(registryNumberOfPages);

        registryService.update(id);

        pdfService.exportRegistryToPdf(id, REGISTRY_PATH);
    }
}