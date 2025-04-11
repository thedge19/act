package com.act.worklog.service;

import com.act.act.dto.ActLogResponseDto;
import com.act.act.dto.ActMapper;
import com.act.act.model.Act;
import com.act.act.repository.ActRepository;
import com.act.subobject.service.SubObjectService;
import com.act.worklog.dto.WorkLogDto;
import com.act.worklog.dto.WorkLogMapper;
import com.act.worklog.model.WorkLog;
import com.act.worklog.repository.WorkLogRepository;
import jakarta.transaction.Transactional;
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
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class WorkLogServiceImplementation implements WorkLogService {

    private final ActRepository actRepository;
    private final WorkLogRepository workLogRepository;
    private final SubObjectService subObjectService;

    @Override
    public List<WorkLogDto> getWorkLog3() {
        List<WorkLog> workLogs = workLogRepository.findAllByOrderByWorkLogNumber();

        return workLogs.stream().map(WorkLogMapper.INSTANCE::toDto).toList();
    }

    @Override
    public List<ActLogResponseDto> getWorkLog6() {
        List<Act> acts = actRepository.findAllByOrderByEndDateAscActNumberAsc();

        return acts.stream().map(ActMapper.INSTANCE::toLogDto).toList();
    }

    @Transactional
    @Override
    public void fillInTheLog3() {

        workLogRepository.deleteAll();

        List<Act> acts = actRepository.findAllByOrderByStartDateAscActNumberAsc();
        List<LocalDate> workLogDates = new ArrayList<>();
        List<Long> subObjectIds = new ArrayList<>();
        Map<LocalDate, Map<Long, List<String>>> logRows = new HashMap<>();

        for (Act act : acts) {
            ArrayList<LocalDate> actDates = new ArrayList<>();
            for (LocalDate d = act.getStartDate(); !d.isAfter(act.getEndDate()); d = d.plusDays(1)) {
                actDates.add(d);
                if (!workLogDates.contains(d)) {
                    workLogDates.add(d);
                }
            }

            Long subObjectId = act.getSubObject().getId();
            subObjectIds.add(subObjectId);
            double workDone = act.getWorkDone().doubleValue();

            if (!subObjectIds.contains(subObjectId)) {
                subObjectIds.add(subObjectId);
            }

            String currentWork = act.getWorks().split(":", 2)[1];

            String[] works = currentWork.split(" ");
            int worksSize = works.length;

            StringBuilder workString = new StringBuilder();
            String units = works[worksSize - 1];

            for (int j = 0; j < worksSize - 3; j++) {
                workString.append(works[j]);
                workString.append(" ");
            }

            currentWork = workString.toString();

            double rowWorkDone = workDone / actDates.size();

            for (int k = 0; k < actDates.size(); k++) {
                LocalDate rowDate = actDates.get(k);

                if (actDates.size() > 1) {
                    if (k != actDates.size() - 1) {
                        workDone = workDone - rowWorkDone;
                    } else {
                        rowWorkDone = workDone;
                    }
                }
                String rowCurrentWork = currentWork + " - " + String.format("%.2f", rowWorkDone) + " " + units;

                if (logRows.get(rowDate) == null) {
                    Map<Long, List<String>> row = new HashMap<>();
                    List<String> rowWorks = new ArrayList<>();
                    rowWorks.add(rowCurrentWork);
                    row.put(subObjectId, rowWorks);

                    logRows.put(actDates.get(k), row);
                } else {
                    if (logRows.get(rowDate).get(subObjectId) == null) {
                        List<String> rowWorks = new ArrayList<>();
                        rowWorks.add(rowCurrentWork);
                        logRows.get(rowDate).put(subObjectId, rowWorks);
                    } else {
                        logRows.get(rowDate).get(subObjectId).add(rowCurrentWork);
                    }
                }
            }
        }

        int i = 1;

        for (LocalDate d : workLogDates) {

            WorkLog workLog = new WorkLog();

            workLog.setWorkDate(d);
            Set<Long> rowKeySet = logRows.get(d).keySet();
            String workLogName = "";

            for (Long rowKey : rowKeySet) {
                workLogName = workLogName + subObjectService.get(rowKey).getName() + ": ";
                for (String work : logRows.get(d).get(rowKey)) {
                    workLogName = workLogName + work + "; ";
                }
            }
            workLog.setName(workLogName);
            workLog.setWorkLogNumber(i);
            workLogRepository.save(workLog);

            i++;
        }
    }

    @Override
    public void fillInTheLog6() {

    }


    @Override
    public void export3InExcel() throws IOException {

        String path = "C:\\Users\\PC\\IdeaProjects\\AOSR\\AOSR\\act\\workLog.xlsx";
        File file = new File(path);

        FileInputStream fis = new FileInputStream(file);
        Workbook workbook = new XSSFWorkbook(fis);

        CellStyler styler = new CellStyler();
        CellStyle style = styler.createCellStyle(workbook);

        Sheet sheet = workbook.getSheetAt(3);

        for (int i = lastRow(sheet) - 1; i >= 4; i--) {
            sheet.removeRow(sheet.getRow(i));
        }

        List<WorkLogDto> dtos = getWorkLog3();

        int rowNumber = 4;

        workbook.setPrintArea(3, 0, 3, 0, 3 + dtos.size());

        for (WorkLogDto dto : dtos) {
            Row createRow = sheet.createRow(rowNumber);

            String rowWorkLogNumber = String.valueOf(dto.getWorkLogNumber());
            Cell numberCell = createRow.createCell(0);
            numberCell.setCellStyle(style);
            CellUtil.createCell(createRow, 0, rowWorkLogNumber == null ? "" : rowWorkLogNumber);

            String rowDate = String.valueOf(dto.getWorkDate());
            Cell dateCell = createRow.createCell(1);
            dateCell.setCellStyle(style);
            CellUtil.createCell(createRow, 1, rowDate == null ? "" : rowDate);

            String rowName = dto.getName();
            Cell nameCell = createRow.createCell(2);
            nameCell.setCellStyle(style);
            CellUtil.createCell(createRow, 2, rowName == null ? "" : rowName);

            String rowDirector = "Руководитель работ Трифонов А.Е.";
            Cell directorCell = createRow.createCell(3);
            directorCell.setCellStyle(style);
            CellUtil.createCell(createRow, 3, rowDirector);

            rowNumber++;
            sheet.createRow(rowNumber);
        }

        workbook.write(new FileOutputStream(file));

        log.info("Выгрузка окончена");

        workbook.close();
        fis.close();
    }

    @Override
    public void export6InExcel() throws IOException {
        String path = "C:\\Users\\PC\\IdeaProjects\\AOSR\\AOSR\\act\\workLog.xlsx";
        File file = new File(path);

        FileInputStream fis = new FileInputStream(file);
        Workbook workbook = new XSSFWorkbook(fis);

        CellStyler styler = new CellStyler();
        CellStyle style = styler.createCellStyle(workbook);

        Sheet sheet = workbook.getSheetAt(6);

        for (int i = lastRow(sheet) - 1; i >= 4; i--) {
            sheet.removeRow(sheet.getRow(i));
        }

        List<ActLogResponseDto> dtos = actRepository
                .findAllByOrderByEndDateAscActNumberAsc()
                .stream()
                .map(ActMapper.INSTANCE::toLogDto)
                .toList();

        int rowNumber = 4;

        workbook.setPrintArea(6, 0, 2, 0, 3 + dtos.size());

        for (ActLogResponseDto dto : dtos) {
            Row createRow = sheet.createRow(rowNumber);

            int rowActNumber = rowNumber - 3;
            Cell numberCell = createRow.createCell(0);
            numberCell.setCellStyle(style);
            CellUtil.createCell(createRow, 0, String.valueOf(rowActNumber));

            String rowAct = "Акт освидетельствования скрытых работ №"
                    + dto.getActNumber() + " "
                    + dto.getWorks();
            Cell actCell = createRow.createCell(1);
            actCell.setCellStyle(style);
            CellUtil.createCell(createRow, 1, rowAct);

            String rowDate = String.valueOf(dto.getEndDate());
            String signs = rowNumber == 4 ? ", Ведущий инженер ОКС ПК «Шесхарис» Челебиев А.А., " +
                    "Руководитель работ ООО «Энергомонтаж» А.Е. Трифонов, " +
                    "Начальник СКК ООО «Энергомонтаж» Попова Л.С." : ", Те же лица, что и в п.1";
            rowDate = rowDate + signs;
            Cell dateCell = createRow.createCell(2);
            dateCell.setCellStyle(style);
            CellUtil.createCell(createRow, 2, rowDate);

            rowNumber++;
            sheet.createRow(rowNumber);
        }

        workbook.write(new FileOutputStream(file));

        log.info("Выгрузка окончена");

        workbook.close();
        fis.close();
    }

    private int lastRow(Sheet sheet) {
        int rowNumber = 4;

        while (!Objects.equals(sheet.getRow(rowNumber).getCell(6), null)) {
            rowNumber++;
        }
        return rowNumber;
    }
}
