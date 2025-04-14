package com.act.registry.service;

import com.act.act.model.Act;
import com.act.act.model.EntranceControl;
import com.act.act.repository.ActRepository;
import com.act.act.repository.EntranceControlRepository;
import com.act.act.service.ActService;
import com.act.exception.exception.InternalErrorException;
import com.act.exception.exception.NotFoundException;
import com.act.pdf.service.PdfService;
import com.act.registry.dto.RegistryDto;
import com.act.registry.dto.RegistryMapper;
import com.act.registry.dto.RegistryResponseDto;
import com.act.registry.dto.RegistryUpdateRequestDto;
import com.act.registry.model.Registry;
import com.act.registry.repository.RegistryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellUtil;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RegistryServiceImplementation implements RegistryService {

    private final RegistryRepository registryRepository;
    private final ActRepository actRepository;
    private final ActService actService;
    private final PdfService pdfService;
    private final EntranceControlRepository entranceControlRepository;
    private final static String ENERGY = "ООО Энергомонтаж";

    @Override
    public Registry findRegistryOrNot(Long id) {
        return registryRepository.findById(id).orElseThrow(() -> new NotFoundException("Строка реестра не найдена"));
    }

    @Override
    public List<RegistryResponseDto> getAllByMonth(int monthId) {
        List<Registry> registries = registryRepository.findAllByOrderByAddingTimeAsc(monthId);
        return registries.stream().map(RegistryMapper.INSTANCE::toDto).collect(Collectors.toList());
    }

    @Transactional
    @Override
    public void create(RegistryDto dto) {

        if (dto.getActId() == null) {
            throw new InternalErrorException("Некорректные данные");
        }

        Act act = actService.findActOrNot(dto.getActId());
        List<EntranceControl> controls = entranceControlRepository.findAllByAct(act);

        long rowCounter = registryRepository.countByMonthId(dto.getMonthId());

        Registry registry = new Registry();
        if (rowCounter == 0) {
            registry.setDocumentName("Реестр исполнительной документации");
            registry.setDocumentNumber("б/н");
            registry.setDocumentAuthor(ENERGY);
            registry.setDocumentDate(LocalDate.of(2025, dto.getMonthId(), 28));
            registry.setMonthId(dto.getMonthId());
            registry.setNumberOfSheets(1);
            registry.setRowNumber(rowCounter + 1);
            registry.setAddingTime(LocalDateTime.now());

            registryRepository.save(registry);
        }


        Registry actRegistry = new Registry();
        assert act != null;

        String documentNumber = act.getActNumber();

        actRegistry.setMonthId(dto.getMonthId());
        actRegistry.setDocumentName("Акт освидетельствования скрытых работ: '" + act.getWorks() + "'");
        actRegistry.setDocumentNumber(documentNumber);
        actRegistry.setDocumentAuthor(ENERGY);
        actRegistry.setDocumentDate(act.getEndDate());
        actRegistry.setNumberOfSheets(1);
        actRegistry.setRowNumber(registryRepository.countByMonthId(dto.getMonthId()) + 1);
        actRegistry.setAddingTime(LocalDateTime.now());

        log.info("ACT {}", actRegistry);
        registryRepository.save(actRegistry);
        act.setInRegistry("in registry");

        Registry schemaRegistry = new Registry();

        schemaRegistry.setMonthId(dto.getMonthId());
        schemaRegistry.setDocumentName("Исполнительная схема: '" + act.getWorks() + "'");
        schemaRegistry.setDocumentNumber(documentNumber);
        schemaRegistry.setDocumentAuthor(ENERGY);
        schemaRegistry.setDocumentDate(act.getEndDate());
        schemaRegistry.setNumberOfSheets(1);
        schemaRegistry.setRowNumber(registryRepository.countByMonthId(dto.getMonthId()) + 1);
        schemaRegistry.setAddingTime(LocalDateTime.now());
        schemaRegistry.setCurrentActId(actRegistry.getId());

        registryRepository.save(schemaRegistry);

        if (!controls.isEmpty()) {
            for (EntranceControl control : controls) {
                Registry controlRegistry = new Registry();
                controlRegistry.setMonthId(dto.getMonthId());
                controlRegistry.setDocumentName("Акт результатов входного контроля МТР и оборудования "
                        + control.getMaterials());
                controlRegistry.setDocumentNumber(control.getControlNumber());
                controlRegistry.setDocumentAuthor(ENERGY);
                controlRegistry.setDocumentDate(control.getDate());
                controlRegistry.setNumberOfSheets(1);
                controlRegistry.setRowNumber(registryRepository.countByMonthId(dto.getMonthId()) + 1);
                controlRegistry.setAddingTime(LocalDateTime.now());
                controlRegistry.setCurrentActId(actRegistry.getId());


                registryRepository.save(controlRegistry);

                Registry certificateRegistry = getCertificateRegistry(dto, control, registryRepository
                        .countByMonthId(dto.getMonthId()));
                certificateRegistry.setCurrentActId(actRegistry.getId());

                registryRepository.save(certificateRegistry);
            }
        }
    }

    @Transactional
    @Override
    public void update(int monthId) throws IOException {
        List<Registry> registries = registryRepository.findAllByOrderByAddingTimeAsc(monthId);

        for (int i=0; i <registries.size(); i++) {
            if (i == 0) {
                int numberOfSheets = pdfService.numberOfPages() % 2 == 0 ? pdfService.numberOfPages() / 2 : pdfService.numberOfPages() / 2 + 1;
                registries.get(i).setNumberOfSheets(numberOfSheets);
                registries.get(i).setListInOrder(numberOfSheets);
            } else {
                registries.get(i).setListInOrder(registries.get(i-1).getListInOrder() + registries.get(i).getNumberOfSheets());
            }
        }
    }

    @Transactional
    @Override
    public void delete(Long id) {
        Registry deletedRegistry = findRegistryOrNot(id);
        if (!deletedRegistry.getDocumentName().startsWith("Реестр")) {
            Act act = actRepository.findByActNumber(deletedRegistry.getDocumentNumber().split(" ")[0]);
            act.setInRegistry(null);
            List<Registry> deletedRegistries = registryRepository.findAllByCurrentActId(id);
            registryRepository.deleteAll(deletedRegistries);
        }
        registryRepository.deleteById(id);
    }


    private static Registry getCertificateRegistry(RegistryDto dto, EntranceControl control, long row) {
        Registry certificateRegistry = new Registry();
        certificateRegistry.setMonthId(dto.getMonthId());

        String certificate = control.getDocuments();
        int delimiterIndex = certificate.indexOf("№");

        String certificateName = certificate.substring(0, delimiterIndex - 1);
        String certificateNumber = certificate.substring(delimiterIndex + 1, certificate.length() - 1);

        certificateRegistry.setDocumentName(certificateName);
        certificateRegistry.setDocumentNumber(certificateNumber);
        certificateRegistry.setDocumentAuthor(control.getAuthor());
        certificateRegistry.setDocumentDate(control.getDate());
        certificateRegistry.setNumberOfSheets(control.getControlSheetNumbers());
        certificateRegistry.setRowNumber(row + 1);
        certificateRegistry.setAddingTime(LocalDateTime.now());

        return certificateRegistry;
    }

    @Override
    public void excelExport(int monthId) throws IOException {

        String path = "C:\\Users\\PC\\IdeaProjects\\AOSR\\AOSR\\act\\registry.xlsx";
        File file = new File(path);

        FileInputStream fis = new FileInputStream(file);
        Workbook workbook = new XSSFWorkbook(fis);

        CellStyler styler = new CellStyler();
        CellStyle style = styler.createWarningColor(workbook);

        Sheet sheet = workbook.getSheetAt(0);

        for (int i = lastRow(sheet) - 1; i >= 14; i--) {
            sheet.removeRow(sheet.getRow(i));
            log.info("row {} removed", i);
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

        List<Registry> registries = registryRepository.findAllByOrderByAddingTimeAsc(monthId);

        int rowNumber = 14;

        workbook.setPrintArea(0, 0, 5, 0, rowNumber + registries.size());

        for (Registry registry : registries) {
            Row row = sheet.createRow(rowNumber);

            String documentDate = " от " + registry.getDocumentDate().format(formatter) + "г.";
            String documentNumber = registry.getDocumentNumber();

            Cell numBerCell = row.createCell(0);
            numBerCell.setCellStyle(style);
            CellUtil.createCell(row, 0, String.valueOf(registry.getRowNumber()));

            Cell documentNameCell = row.createCell(1);
            documentNameCell.setCellStyle(style);
            CellUtil.createCell(row, 1, registry.getDocumentName());

            Cell documentNumberCell = row.createCell(2);
            documentNumberCell.setCellStyle(style);
            CellUtil.createCell(row, 2, documentNumber + documentDate);

            Cell authorCell = row.createCell(3);
            authorCell.setCellStyle(style);
            CellUtil.createCell(row, 3, registry.getDocumentAuthor());

            Cell numberOfSheetsCell = row.createCell(4);
            numberOfSheetsCell.setCellStyle(style);
            CellUtil.createCell(row, 4, String.valueOf(registry.getNumberOfSheets()));

            Cell listInOrderCell = row.createCell(5);
            listInOrderCell.setCellStyle(style);
            CellUtil.createCell(row, 5, String.valueOf(registry.getListInOrder()));

            rowNumber++;
        }

        log.info("Выгрузка окончена");

        workbook.write(new FileOutputStream(file));

        workbook.close();
        fis.close();
    }

    private int lastRow(Sheet sheet) {
        int rowNumber = 13;

        while (!Objects.equals(sheet.getRow(rowNumber), null)) {

            rowNumber++;
        }

        return rowNumber;
    }
}