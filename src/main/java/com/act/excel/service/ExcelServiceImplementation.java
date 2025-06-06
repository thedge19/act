package com.act.excel.service;

import com.act.act.dto.ActResponseDto;
import com.act.act.dto.EntranceControlExportDto;
import com.act.act.dto.EntranceControlMapper;
import com.act.act.model.Act;
import com.act.act.model.EntranceControl;
import com.act.act.model.SelectedPeriod;
import com.act.act.repository.EntranceControlRepository;
import com.act.act.service.ActService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellUtil;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Slf4j
@Service
@RequiredArgsConstructor
public class ExcelServiceImplementation implements ExcelService {
    private final ActService actService;
    private final EntranceControlRepository entranceControlRepository;

    String path = "C:\\Users\\PC\\IdeaProjects\\AOSR\\act\\act.xlsx";
    File file = new File(path);

    @Override
    public void writeExcel(SelectedPeriod selectedPeriod) throws IOException {

        removeSheets();

        FileInputStream fis = new FileInputStream(file);
        Workbook workbook = new XSSFWorkbook(fis);

        log.info(selectedPeriod.getStartDate());
        log.info(selectedPeriod.getEndDate());

        LocalDate startDate = actService.jsDateToLocalDate(selectedPeriod.getStartDate());

        LocalDate endDate = actService.jsDateToLocalDate(selectedPeriod.getEndDate());

        List<ActResponseDto> acts = actService.findAllByEndDateBetween(startDate, endDate);
        int i = 2;

        for (ActResponseDto act : acts) {

            workbook.cloneSheet(0);
            Sheet newSheet = workbook.getSheetAt(i);

            newSheet.setFitToPage(true);
            newSheet.getPrintSetup().setFitWidth((short) 1);
            newSheet.getPrintSetup().setFitHeight((short) 0);

            String actNumber = act.getActNumber();
            String newSheetName = actNumber.replace("/", "_");

            workbook.setSheetName(i, newSheetName);

            String projectName = clearProjectName(act.getProjectName());
            newSheet.getRow(7).getCell(0).setCellValue(projectName);
            newSheet.getRow(43).getCell(4).setCellValue(actNumber);
            addDate(act.getEndDate(), 43, newSheet, 76, 85, 101);
            addDate(act.getStartDate(), 120, newSheet, 30, 39, 55);
            addDate(act.getEndDate(), 121, newSheet, 30, 39, 55);
            addLongString(act.getWorks(), 85, newSheet);
            newSheet.getRow(93).getCell(0).setCellValue(act.getProjectName());
            addLongString(act.getMaterials(), 99, newSheet);
            addLongString(act.getSubmittedDocuments(), 110, newSheet);
            addLongString(act.getInAccordWith(), 124, newSheet);
            addLongString(act.getSubmittedDocuments(), 142, newSheet);

            i++;

            if (!act.getMaterials().isEmpty()) {

                Act currentAct = actService.findActOrNot(act.getId());
                List<EntranceControl> controls = actService.controls(currentAct);

                int j = 1;

                for (EntranceControl control : controls) {
                    workbook.cloneSheet(1);
                    Sheet controlSheet = workbook.getSheetAt(i);

                    controlSheet.setFitToPage(true);
                    controlSheet.getPrintSetup().setFitWidth((short) 1);
                    controlSheet.getPrintSetup().setFitHeight((short) 0);

                    String controlNumber;
                    if (controls.size() == 1) {
                        controlNumber = actNumber;
                    } else {
                        controlNumber = actNumber + "-" + j;
                    }
                    String controlSheetName = "ВК" + controlNumber.replace("/", "_");
                    workbook.setSheetName(i, controlSheetName);

                    String controlDate = control.getDate().toString();
                    String[] controlDateList = controlDate.split("-");
                    controlDate = controlDateList[2] + " " + getMonth(controlDateList[1]) + " " + controlDateList[0] + " г.";
                    String controlProjectName = clearProjectNameForControls(act.getProjectName());

                    controlSheet.getRow(2).getCell(0).setCellValue(projectName);
                    controlSheet.getRow(5).getCell(4).setCellValue(controlNumber);
                    addLongString(control.getMaterials(), 7, controlSheet);
                    controlSheet.getRow(12).getCell(4).setCellValue(controlDate);
                    addLongString(control.getMaterials(), 29, controlSheet);
                    controlSheet.getRow(34).getCell(4).setCellValue(controlProjectName);
                    controlSheet.getRow(37).getCell(0).setCellValue(control.getSubObjectName());
                    addLongString(control.getMaterials(), 42, controlSheet);
                    addLongString(control.getDocuments(), 48, controlSheet);
                    controlSheet.getRow(56).getCell(3).setCellValue(control.getStandard());
                    addLongString(control.getDocuments(), 63, controlSheet);
                    j++;
                    i++;
                }
            }
            workbook.write(new FileOutputStream(file));
        }
        workbook.close();
        fis.close();
    }

    @Override
    public void removeSheets() throws IOException {
        FileInputStream fis = new FileInputStream(file);
        Workbook workbook = new XSSFWorkbook(fis);

        int total = workbook.getNumberOfSheets();

        for (int i = total - 1; i >= 2; i--) {
            workbook.removeSheetAt(i);
            log.info("Removed sheet {}", i);
            workbook.write(new FileOutputStream(file));
        }
        workbook.close();
        fis.close();
    }

    @Override
    public void writeExcelControl() throws IOException {
        String path = "C:\\Users\\PC\\IdeaProjects\\AOSR\\act\\entrance_control.xlsx";
        File file = new File(path);

        log.info("Здесь");

        FileInputStream fis = new FileInputStream(file);
        Workbook workbook = new XSSFWorkbook(fis);

        CellStyler styler = new CellStyler();
        CellStyle style = styler.createWarningColor(workbook);

        Sheet sheet = workbook.getSheetAt(0);
        int rowNumber = 95;

//        for (int i = lastRow(sheet) - 1; i >= rowNumber; i--) {
//            sheet.removeRow(sheet.getRow(i));
//        }

        List<EntranceControlExportDto> controls = entranceControlRepository
                .findAllByOrderByDateAsc()
                .stream()
                .map(EntranceControlMapper.INSTANCE::toDto).toList();

        int counter = 1;

        log.info(sheet.getSheetName());

        workbook.setPrintArea(0, 0, 8, 0, 94 + controls.size());

        for (EntranceControlExportDto control : controls) {
            Row createRow = sheet.createRow(rowNumber);

            Cell numberCell = createRow.createCell(0);
            numberCell.setCellStyle(style);
            CellUtil.createCell(createRow, 0, String.valueOf(counter));

            Cell dateCell = createRow.createCell(1);
            dateCell.setCellStyle(style);
            CellUtil.createCell(createRow, 1, String.valueOf(control.getDate()));

            Cell controlObjectCell = createRow.createCell(2);
            controlObjectCell.setCellStyle(style);
            String materials = control.getMaterials().split(" - ")[0];
            CellUtil.createCell(createRow, 2, String.valueOf(materials));

            Cell quantityCell = createRow.createCell(3);
            quantityCell.setCellStyle(style);
            String quantity = control.getMaterials().split(" - ")[1];
            CellUtil.createCell(createRow, 3, String.valueOf(quantity));

            Cell certificateCell = createRow.createCell(4);
            certificateCell.setCellStyle(style);
            CellUtil.createCell(createRow, 4, String.valueOf(control.getDocuments()));

            Cell storageConditionsCell = createRow.createCell(5);
            storageConditionsCell.setCellStyle(style);
            CellUtil.createCell(createRow, 5, "Скл. хран.");

            Cell firstEmptyCell = createRow.createCell(6);
            firstEmptyCell.setCellStyle(style);
            CellUtil.createCell(createRow, 6, "");

            Cell secondEmptyCell = createRow.createCell(7);
            secondEmptyCell.setCellStyle(style);
            CellUtil.createCell(createRow, 7, "");

            Cell suitableCell = createRow.createCell(8);
            suitableCell.setCellStyle(style);
            CellUtil.createCell(createRow, 8, "Годен");

            counter++;
            rowNumber++;
        }

        log.info("Последняя строка {}", lastRow(sheet));

        workbook.write(new FileOutputStream(file));
        workbook.close();
        fis.close();
    }

    private String getMonth(String month) {
        List<String> cyphers = List.of("01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12");
        List<String> months = List.of("января", "февраля", "марта", "апреля", "мая", "июня", "июля",
                "августа", "сентября", "октября", "ноября", "декабря");

        Map<String, String> monthsMap = IntStream.range(0, cyphers.size())
                .boxed()
                .collect(Collectors.toMap(cyphers::get, months::get));
        return monthsMap.get(month);
    }

    private String clearProjectName(String projectName) {
        String[] split = projectName.split("\\.");
        return split[0] + ". " + split[1];
    }

    private String clearProjectNameForControls(String projectName) {
        String[] split = projectName.split("\\.");
        return split[2] + ".";
    }

    public void addLongString(String works, int rowNumber, Sheet sheet) {
        while (works.length() >= 98) {
            String worksRow = works.substring(0, 97);
            int lastSpace = worksRow.lastIndexOf(" ");
            worksRow = worksRow.substring(0, lastSpace);
            sheet.getRow(rowNumber).getCell(0).setCellValue(worksRow);
            works = works.replace(worksRow, "");
            rowNumber++;
            Row row = sheet.getRow(rowNumber);
            row.setZeroHeight(false);
        }
        sheet.getRow(rowNumber).getCell(0).setCellValue(works);
    }

    public void addDate(String date, int rowNumber, Sheet sheet, int dayColumnNumber,
                        int monthColumnNumber, int yearColumnNumber) {
        String[] endDateList = date.split("-");
        String day = endDateList[0];
        String month = getMonth(endDateList[1]);
        String year = endDateList[2];

        sheet.getRow(rowNumber).getCell(dayColumnNumber).setCellValue(day);
        sheet.getRow(rowNumber).getCell(monthColumnNumber).setCellValue(month);
        sheet.getRow(rowNumber).getCell(yearColumnNumber).setCellValue(year);
    }

    private int lastRow(Sheet sheet) {
        int rowNumber = 95;

        while (!Objects.equals(sheet.getRow(rowNumber), null)) {
            rowNumber++;
        }
        return rowNumber;
    }
}
