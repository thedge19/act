package com.act.pdf.service;

import com.act.act.dto.*;
import com.act.act.model.Act;
import com.act.act.model.EntranceControl;
import com.act.act.model.ExecutiveSchema;
import com.act.act.model.SelectedPeriod;
import com.act.act.repository.ActRepository;
import com.act.act.repository.EntranceControlRepository;
import com.act.act.repository.ExecutiveSchemaRepository;
import com.act.act.service.ActService;
import com.act.registry.model.Registry;
import com.act.registry.repository.RegistryRepository;
import com.act.worklog.dto.WorkLogDto;
import com.act.worklog.service.WorkLogService;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.awt.image.ImagingOpException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@Slf4j
@RequiredArgsConstructor
public class PdfServiceImplementation implements PdfService {

    public static final String FONT = "./src/main/resources/fonts/timesnewromanpsmt.ttf";
    private final Font f1 = FontFactory.getFont(FONT, "cp1251", true, 11);
    private final Font f2 = FontFactory.getFont(FONT, "cp1251", true, 12, Font.BOLD);
    private final Font f3 = FontFactory.getFont(FONT, "cp1251", true, 9, Font.ITALIC);
    private final Font f4 = FontFactory.getFont(FONT, "cp1251", true, 14, Font.BOLD);
    private final Font f5 = FontFactory.getFont(FONT, "cp1251", true, 9);
    private final Font f6 = FontFactory.getFont(FONT, "cp1251", true, 10, Font.ITALIC);
    private final Font f7 = FontFactory.getFont(FONT, "cp1251", true, 30, Font.BOLD);
    private final Font f8 = FontFactory.getFont(FONT, "cp1251", true, 16, Font.BOLD);
    private final Font f9 = FontFactory.getFont(FONT, "cp1251", true, 14);
    private final Font f10 = FontFactory.getFont(FONT, "cp1251", true, 9);
    private final Font fontToFillIn = FontFactory.getFont(FONT, "cp1251", true, 9, Font.BOLDITALIC);
    private final Font fontToFillInControl = FontFactory.getFont(FONT, "cp1251", true, 11, Font.BOLDITALIC);
    private final Font subscript = FontFactory.getFont(FONT, "cp1251", true, 6);
    private final Font f13 = FontFactory.getFont(FONT, "cp1251", true, 11, Font.BOLD);
    private final Font f14 = FontFactory.getFont(FONT, "cp1251", true, 10, Font.BOLD);
    private final RegistryRepository registryRepository;
    private final ActService actService;
    private final ActRepository actRepository;
    private final WorkLogService workLogService;
    private final EntranceControlRepository entranceControlRepository;
    private final ExecutiveSchemaRepository executiveSchemaRepository;
    private final PdfCellStyler cellStyler = new PdfCellStyler();
    public static final String REGISTRY_PATH = "C:\\Users\\PC\\Desktop\\work\\registry.pdf";
    public static final String REGISTRY_TEMP_PATH = "C:\\Users\\PC\\Desktop\\work\\temp_registry.pdf";
    public static final String WORK_LOG_PATH = "C:\\Users\\PC\\Desktop\\work\\workLog.pdf";
    public static final String WORK_LOG_0_PATH = "C:\\Users\\PC\\IdeaProjects\\AOSR\\AOSR\\act\\workLog0.pdf";
    public static final String WORK_LOG_3_PATH = "C:\\Users\\PC\\Desktop\\work\\workLog3.pdf";
    public static final String WORK_LOG_6_PATH = "C:\\Users\\PC\\Desktop\\work\\workLog6.pdf";
    public static final String ENTRANCE_CONTROL_PATH = "C:\\Users\\PC\\Desktop\\work\\entranceControlLog.pdf";
    public static final String ACTS_FOLDER = "C:\\Users\\PC\\Desktop\\work\\acts\\";
    public static final String MERGED_ACTS_FOLDER = "C:\\Users\\PC\\Desktop\\work\\merged\\";

    @Override
    public void exportRegistryToPdf(int monthId, String path) throws IOException, DocumentException {

        deleteFile(Paths.get(REGISTRY_TEMP_PATH));
        deleteFile(Paths.get(REGISTRY_PATH));

        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream(path));
        document.open();

        PdfPTable table = new PdfPTable(6);
        table.setWidthPercentage(105);

        table.setTotalWidth(500f);
        float[] widths = new float[]{20f, 200f, 90f, 78f, 52f, 60f};
        table.setWidths(widths);

        List<Registry> registries = registryRepository.findAllByOrderByAddingTimeAsc(monthId);

        addRegistryTableData(table, registries);

        document.add(table);

        document.close();

        log.info("Export to PDF successful");
    }

    private void addRegistryTableData(PdfPTable table, List<Registry> registries) {
        addPdfRegistryHeader(table);
        addTableRegistryHeader(table);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

        for (Registry registry : registries) {
            String documentDate = " от " + registry.getDocumentDate().format(formatter) + "г.";

            String documentNumber = registry.getDocumentNumber();

            table.addCell(createCell(String.valueOf(registry.getRowNumber()), "centerBorder", f1, 1, 1, 0.0F));
            table.addCell(createCell(String.valueOf(registry.getDocumentName()), "centerBorder", f1, 1, 1, 0.0F));
            table.addCell(createCell(documentNumber + documentDate, "centerBorder", f1, 1, 1, 0.0F));
            table.addCell(createCell(registry.getDocumentAuthor(), "centerBorder", f1, 1, 1, 0.0F));
            table.addCell(createCell(String.valueOf(registry.getNumberOfSheets()), "centerBorder", f1, 1, 1, 0.0F));
            table.addCell(createCell(String.valueOf(registry.getListInOrder()), "centerBorder", f1, 1, 1, 0.0F));
        }

        addPdfFooter(table);
    }

    private void addPdfRegistryHeader(PdfPTable table) {
//      1 строка
        table.addCell(createCell("", "centerNoBorder", f1, 3, 1, 0.0F));
        table.addCell(createCell("Форма", "leftCenterNoBorder", f1, 1, 1, 0.0F));
        table.addCell(createCell("№1.2", "leftCenterNoBorder", f1, 2, 1, 0.0F));
//      2 строка
        table.addCell(createCell("Заказчик: АО «Черномортранснефть»", "leftCenterNoBorder", f1, 3, 1, 0.0F));
        table.addCell(createCell("Основание", "leftCenterNoBorder", f1, 1, 1, 0.0F));
        table.addCell(createCell("ВСН 012-88 (часть II)", "leftCenterNoBorder", f1, 2, 1, 0.0F));
//      3 строка
        table.addCell(createCell("Подрядчик: ООО «Энергомонтаж»", "leftTopNoBorder", f1, 3, 1, 0.0F));
        table.addCell(createCell("Строительство", "leftTopNoBorder", f1, 1, 1, 0.0F));
        table.addCell(createCell("14.295.24 ТЕКУЩИЙ РЕМОНТ ЗДАНИЙ И СООРУЖЕНИЙ ПК «ШЕСХАРИС»", "leftCenterNoBorder", f1, 2, 1, 0.0F));
//      4 строка
        table.addCell(createCell("Субподрядчик:", "leftTopNoBorder", f1, 3, 1, 0.0F));
        table.addCell(createCell("Объект: ", "leftTopNoBorder", f1, 1, 1, 0.0F));
        table.addCell(createCell("«Текущий ремонт зданий ПП «Грушовая» ПК «Шесхарис». " +
                "Текущий ремонт». «Текущий ремонт зданий ПП «Шесхарис». " +
                "ПК «Шесхарис». Текущий ремонт", "leftTopNoBorder", f1, 2, 1, 0.0F));
//      Заголовок
        table.addCell(createCell("РЕЕСТР", "centerBottomNoBorder", f1, 6, 1, 50F));
        table.addCell(createCell("исполнительной  документации", "centerTopNoBorder", f1, 6, 1, 30F));
    }

    private void addTableRegistryHeader(PdfPTable table) {
        table.addCell(createCell("№ п/п", "centerBorder", f2, 1, 1, 0.0F));
        table.addCell(createCell("Наименование документа", "centerBorder", f2, 1, 1, 0.0F));
        table.addCell(createCell("№ документа, дата", "centerBorder", f2, 1, 1, 0.0F));
        table.addCell(createCell("Организация, составившая документ", "centerBorder", f2, 1, 1, 0.0F));
        table.addCell(createCell("Кол-во листов", "centerBorder", f2, 1, 1, 0.0F));
        table.addCell(createCell("Страница по списку", "centerBorder", f2, 1, 1, 0.0F));
    }

    private void addPdfFooter(PdfPTable table) {
        table.addCell(createCell("Сдал", "leftBottomNoBorder", f1, 2, 1, 50F));
        addRegistrySignature(table);
        table.addCell(createCell("Принял", "leftBottomNoBorder", f1, 2, 1, 40F));
        addRegistrySignature(table);
    }

    private void addRegistrySignature(PdfPTable table) {
        table.addCell(createCell("", "emptyCellBottomBorder", f1, 4, 1, 50F));
        table.addCell(createCell("", "centerNoBorder", f1, 2, 1, 0.0F));
        table.addCell(createCell("(фамилия, инициалы)", "centerTopNoBorder", f3, 2, 1, 0.0F));
        table.addCell(createCell("(подпись)", "centerTopNoBorder", f3, 1, 1, 0.0F));
        table.addCell(createCell("(дата)", "centerTopNoBorder", f3, 1, 1, 0.0F));
    }

    @Override
    public void exportWorkLog3ToPdf() throws IOException, DocumentException {

        deleteFile(Paths.get(WORK_LOG_3_PATH));

        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream(WORK_LOG_3_PATH));
        document.open();

        PdfPTable table = new PdfPTable(4);
        table.setWidthPercentage(105);

        table.setTotalWidth(500f);
        float[] widths = new float[]{31.11f, 79.89f, 267.07f, 121.93f};
        table.setWidths(widths);

        List<WorkLogDto> dtos3 = workLogService.getWorkLog3();

        addWorkLog3TableData(table, dtos3);

        document.add(table);

        document.close();

        log.info("Export to PDF successful");

    }

    @Override
    public void exportWorkLog6ToPdf() throws IOException, DocumentException {

        deleteFile(Paths.get(WORK_LOG_6_PATH));

        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream(WORK_LOG_6_PATH));
        document.open();

        PdfPTable table = new PdfPTable(3);
        table.setWidthPercentage(105);

        table.setTotalWidth(500f);
        float[] widths6 = new float[]{19.92f, 335.92f, 144.16f};
        table.setWidths(widths6);

        List<ActLogResponseDto> dtos = actRepository
                .findAllByOrderByEndDateAscActNumberAsc()
                .stream()
                .map(ActMapper.INSTANCE::toLogDto)
                .toList();
        addWorkLog6TableData(table, dtos);

        document.add(table);

        document.close();

        log.info("Export to PDF successful");
    }

    public void exportEntranceControlLogToPdf() throws IOException, DocumentException {
        deleteFile(Paths.get(ENTRANCE_CONTROL_PATH));

        Document document = new Document();

        document.setPageSize(PageSize.A4.rotate());

        PdfWriter.getInstance(document, new FileOutputStream(ENTRANCE_CONTROL_PATH));
        document.open();

        PdfPTable table = new PdfPTable(9);

        table.setWidthPercentage(105);
        table.setTotalWidth(500f);
        float[] widths6 = new float[]{19.93f, 38.71f, 98.51f, 47.84f, 138.38f, 41.58f, 33.61f, 34.17f, 47.28f};
        table.setWidths(widths6);

        addEntranceControlLogTableData(table);
        document.add(table);

        document.close();

        log.info("Export to PDF successful");
    }


    public void exportAOSRtoPdf(SelectedPeriod selectedPeriod) throws IOException, DocumentException {

        LocalDate startDate = jsDateToLocalDate(selectedPeriod.getStartDate());
        LocalDate endDate = jsDateToLocalDate(selectedPeriod.getEndDate());


        List<ActResponseDto> acts = actService.findAllByEndDateBetween(startDate, endDate);

        for (ActResponseDto act : acts) {
            Document document = new Document();
            List<PdfReader> pdfReaders = new ArrayList<>();
            String actNumber = act.getActNumber().replace("/", "_");
            String localPath = ACTS_FOLDER + actNumber + ".pdf";
            PdfWriter.getInstance(document, new FileOutputStream(localPath));
            document.open();

            String currentPath = MERGED_ACTS_FOLDER + actNumber + ".pdf";

            PdfPTable table = new PdfPTable(36);

            table.setWidthPercentage(105);
            table.setTotalWidth(36f);
            float[] widths = new float[36];

            for (int i = 0; i < 36; i++) {
                widths[i] = 1f;
            }

            table.setWidths(widths);

            addAOSRTableData(table, act);
            document.add(table);

            document.close();
            log.info("Акт {} создан", act.getActNumber());

            pdfReaders.add(new PdfReader(localPath));

            ExecutiveSchema schema = executiveSchemaRepository.findById(act.getExecutiveSchemaId()).orElse(null);

            assert schema != null;
            pdfReaders.add(new PdfReader(schema.getSchemaPath()));

            if (!act.getMaterials().isEmpty()) {
                Act currentAct = actService.findActOrNot(act.getId());
                List<EntranceControl> controls = actService.controls(currentAct);

                for (EntranceControl control : controls) {
                    Document controlDocument = new Document();

                    String controlNumber = control.getControlNumber().replace("/", "_");
                    String controlLocalPath = ACTS_FOLDER + "EC" + controlNumber + ".pdf";

                    PdfWriter.getInstance(controlDocument, new FileOutputStream(controlLocalPath));
                    controlDocument.open();

                    PdfPTable controlTable = new PdfPTable(9);
                    controlTable.setWidthPercentage(105);

                    controlTable.setTotalWidth(500f);
                    float[] controlWidths = new float[]{48.76f, 89.25f, 48.76f, 60.33f, 57.03f, 48.76f, 42.17f, 71.90f, 33.03f};
                    controlTable.setWidths(controlWidths);

                    addControlTableData(controlTable, control, act);
                    controlDocument.add(controlTable);

                    controlDocument.close();
                    log.info("Акт входного конроля {} создан", control.getControlNumber());

                    pdfReaders.add(new PdfReader(controlLocalPath));

                    String certificatePath = control.getMaterial().getCertificate().getPath();

                    pdfReaders.add(new PdfReader(certificatePath));
                }
            }

            mergeUsingIText(pdfReaders, currentPath);
        }
    }

    // workLog
    // --------------------------------------------------------------------------------------------------------------------------------
    private void addWorkLog3TableData(PdfPTable table, List<WorkLogDto> dtos3) {
        addPdfWorkLog3Header(table);
        addPdfWorkLog3TableHeader(table);

        for (WorkLogDto dto : dtos3) {
            table.addCell(createCell(String.valueOf(dto.getWorkLogNumber()), "centerBorder", f3, 1, 1, 0.0F));
            table.addCell(createCell(String.valueOf(dto.getWorkDate()), "centerBorder", f3, 1, 1, 0.0F));
            table.addCell(createCell(String.valueOf(dto.getName()), "centerBorder", f3, 1, 1, 0.0F));
            table.addCell(createCell("Руководитель работ Трифонов А.Е.", "centerBorder", f3, 1, 1, 0.0F));
        }

    }

    private void addPdfWorkLog3Header(PdfPTable table) {
        table.addCell(createCell("РАЗДЕЛ 3", "centerNoBorder", f4, 4, 1, 0.0F));
        table.addCell(createCell("Сведения о выполнении работ в процессе строительства, \n" +
                "реконструкции, капитального ремонта объекта капитального строительства", "centerNoBorder", f5, 4, 1, 50F));
    }

    private void addPdfWorkLog3TableHeader(PdfPTable table) {
        table.addCell(createCell("№№/пп", "centerBorder", f6, 1, 1, 0.0F));
        table.addCell(createCell("Дата выполнения работ", "centerBorder", f6, 1, 1, 0.0F));
        table.addCell(createCell("Наименование работ, выполняемых  в процессе строительства, " +
                "реконструкции, капитального ремонта объекта капитального строительства", "centerBorder", f6, 1, 1, 0.0F));
        table.addCell(createCell("Должность, фамилия, инициалы, подпись уполномоченного представителя лица, " +
                "осуществляющего строительство", "centerBorder", f6, 1, 1, 0.0F));
        table.addCell(createCell("1", "centerBorder", f3, 1, 1, 0.0F));
        table.addCell(createCell("2", "centerBorder", f3, 1, 1, 0.0F));
        table.addCell(createCell("3", "centerBorder", f3, 1, 1, 0.0F));
        table.addCell(createCell("4", "centerBorder", f3, 1, 1, 0.0F));
    }

    private void addWorkLog6TableData(PdfPTable table, List<ActLogResponseDto> dtos6) {
        addPdfWorkLog6Header(table);
        addPdfWorkLog6TableHeader(table);

        int rowNumber = 1;

        for (ActLogResponseDto dto : dtos6) {

            table.addCell(createCell(String.valueOf(rowNumber), "centerBorder", f3, 1, 1, 0.0F));

            String rowAct = "Акт освидетельствования скрытых работ №"
                    + dto.getActNumber() + " "
                    + dto.getWorks();
            table.addCell(createCell(rowAct, "centerBorder", f3, 1, 1, 0.0F));

            String rowDateAndSigns = String.valueOf(dto.getEndDate());

            String signs = rowNumber == 1 ? "г., Ведущий инженер ОКС ПК «Шесхарис» Челебиев А.А., " +
                    "Руководитель работ ООО «Энергомонтаж» А.Е. Трифонов, " +
                    "Начальник СКК ООО «Энергомонтаж» Попова Л.С." : "г., Те же лица, что и в п.1";

            rowDateAndSigns += signs;

            table.addCell(createCell(rowDateAndSigns, "centerBorder", f3, 1, 1, 0.0F));

            rowNumber++;
        }

    }

    private void addPdfWorkLog6Header(PdfPTable table) {
        table.addCell(createCell("РАЗДЕЛ 6", "centerNoBorder", f4, 4, 1, 0.0F));
        table.addCell(createCell("Перечень исполнительной документации при строительстве, \n" +
                "реконструкции, капитальном ремонте объекта капитального строительства", "centerNoBorder", f5, 4, 1, 50F));
    }

    private void addPdfWorkLog6TableHeader(PdfPTable table) {
        table.addCell(createCell("№№/пп", "centerBorder", f6, 1, 1, 0.0F));
        table.addCell(createCell("Наименование исполнительной документации (с указанием вида работ, места " +
                        "расположения конструкций, участков сетей инженерно – технического обеспечения и т.д.)",
                "centerBorder", f6, 1, 1, 0.0F));
        table.addCell(createCell("Дата подписания акта, должности, фамилии, инициалы лиц, подписавших акты",
                "centerBorder", f6, 1, 1, 0.0F));
        table.addCell(createCell("1", "centerBorder", f3, 1, 1, 0.0F));
        table.addCell(createCell("2", "centerBorder", f3, 1, 1, 0.0F));
        table.addCell(createCell("3", "centerBorder", f3, 1, 1, 0.0F));
    }

    // EntranceControlLog
    // --------------------------------------------------------------------------------------------------------------------------------
    private void addEntranceControlLogTableData(PdfPTable table) {
        addEntranceControlLogTitle(table);

        addEntranceControlLogTableHeader(table);

        List<EntranceControlExportDto> controls = entranceControlRepository
                .findAllByOrderByDateAsc()
                .stream()
                .map(EntranceControlMapper.INSTANCE::toDto).toList();

        int counter = 1;

        for (EntranceControlExportDto control : controls) {
            table.addCell(createCell(counter + "", "centerBorder", f3, 1, 1, 0.0F));
            table.addCell(createCell(String.valueOf(control.getDate()), "centerBorder", f3, 1, 1, 0.0F));
            table.addCell(createCell(control.getMaterials().split(" - ")[0], "centerBorder", f3, 1, 1, 0.0F));
            table.addCell(createCell(control.getMaterials().split(" - ")[1], "centerBorder", f3, 1, 1, 0.0F));
            table.addCell(createCell(control.getDocuments(), "centerBorder", f3, 1, 1, 0.0F));
            table.addCell(createCell("Скл. хран.", "centerBorder", f3, 1, 1, 0.0F));
            table.addCell(createCell("", "centerBorder", f3, 1, 1, 0.0F));
            table.addCell(createCell("", "centerBorder", f3, 1, 1, 0.0F));
            table.addCell(createCell("Годен", "centerBorder", f3, 1, 1, 0.0F));
        }
    }

    private void addEntranceControlLogTitle(PdfPTable table) {
        table.addCell(createCell("Журнал", "centerBottomNoBorder", f7, 9, 1, 200F));
        table.addCell(createCell("входного учета и контроля качества получаемых деталей,", "centerBottomNoBorder", f8, 9, 1, 0.0F));
        table.addCell(createCell("материалов, конструкций и оборудования", "centerBottomNoBorder", f8, 9, 1, 0.0F));
        table.addCell(createCell("на объекте:", "centerNoBorder", f9, 9, 1, 80F));
        table.addCell(createCell("14.295.24 «Текущий ремонт зданий и сооружений ПК «Шесхарис»", "centerTopNoBorder", f9, 9, 1, 200F));
        table.addCell(createCell("", "centerTopNoBorder", f9, 9, 1, 520F));
        table.addCell(createCell("ООО «Энергомонтаж»", "leftBottomNoBorder", f1, 8, 1, 20F));
        table.addCell(createCell("Форма 12", "rightBottomNoBorder", f1, 1, 1, 20F));
        table.addCell(createCell("(Наименование предприятия)", "leftCenterNoBorder", f1, 6, 1, 0.0F));
        table.addCell(createCell("РД 39-00147105-015-98", "rightCenterNoBorder", f1, 3, 1, 0.0F));
        table.addCell(createCell("Строительство: Текущий ремонт", "rightCenterNoBorder", f1, 9, 1, 0.0F));
        table.addCell(createCell("Объект: 14.295.24 «Текущий ремонт зданий и сооружений ПК «Шесхарис»", "rightTopNoBorder", f1, 9, 1, 110F));
        table.addCell(createCell("Журнал", "centerTopNoBorder", f9, 9, 1, 0.0F));
        table.addCell(createCell("входного контроля качества", "centerTopNoBorder", f9, 9, 1, 0.0F));
        table.addCell(createCell("", "centerBottomNoBorder", f1, 5, 1, 120F));
        table.addCell(createCell("Начат:", "leftBottomNoBorder", f1, 1, 1, 120F));
        table.addCell(createCell("« 02 » сентября 2024г.", "leftBottomNoBorder", f1, 3, 1, 120F));
        table.addCell(createCell("", "centerBottomNoBorder", f1, 5, 1, 20F));
        table.addCell(createCell("Окончен:", "leftBottomNoBorder", f1, 1, 1, 20F));
        table.addCell(createCell("«      »                 20__г.", "leftBottomNoBorder", f1, 3, 1, 20F));
        table.addCell(createCell("Руководитель подрядной организации", "leftBottomNoBorder", f1, 4, 1, 110F));
        table.addCell(createCell("__________________________", "centerBottomNoBorder", f1, 1, 1, 110F));
        table.addCell(createCell("_________________", "centerBottomNoBorder", f1, 2, 1, 110F));
        table.addCell(createCell("_________________", "centerBottomNoBorder", f1, 2, 1, 110F));
        table.addCell(createCell("М.П.", "leftTopNoBorder", f1, 4, 1, 20F));
        table.addCell(createCell("Фамилия, инициалы", "centerTopNoBorder", f1, 1, 1, 70F));
        table.addCell(createCell("Подпись", "centerTopNoBorder", f1, 2, 1, 70F));
        table.addCell(createCell("Дата", "centerTopNoBorder", f1, 2, 1, 70F));


    }

    private void addEntranceControlLogTableHeader(PdfPTable table) {
        table.addCell(createCell("№ п/п", "centerBorder", f3, 1, 2, 0.0F));
        table.addCell(createCell("Дата поставки", "centerBorder", f3, 1, 2, 0.0F));
        table.addCell(createCell("Объект контроля", "centerBorder", f3, 1, 2, 0.0F));
        table.addCell(createCell("Количество ед. измерения", "centerBorder", f3, 1, 2, 0.0F));
        table.addCell(createCell("Номер партии, сертификат, тех.паспорт", "centerBorder", f3, 1, 2, 0.0F));
        table.addCell(createCell("Условия хранения", "centerBorder", f3, 1, 2, 0.0F));
        table.addCell(createCell("Подпись принявших продукцию по качеству", "centerBorder", f3, 2, 1, 0.0F));
        table.addCell(createCell("Определение степени годности", "centerBorder", f3, 1, 2, 0.0F));

        table.addCell(createCell("Исполни-тель работ", "centerBorder", f3, 1, 1, 0.0F));
        table.addCell(createCell("Контроллёр", "centerBorder", f3, 1, 1, 0.0F));
    }

    // АОСР
    // --------------------------------------------------------------------------------------------------------------------------------
    private void addAOSRTableData(PdfPTable table, ActResponseDto act) {
        addFirstStaticBlock(table);

        table.addCell(createCell(act.getProjectName(), "centerBorderBottom", fontToFillIn, 36, 1, 0.0F));

        addSecondStaticBlock(table);

        table.addCell(createCell("№", "rightBottomNoBorder", f14, 2, 1, 20F));
        table.addCell(createCell(act.getActNumber(), "centerBottomBorderBottom", fontToFillIn, 5, 1, 20F));
        table.addCell(createCell("", "rightBottomNoBorder", f14, 16, 1, 20F));
        table.addCell(createCell("«", "rightBottomNoBorder", f5, 1, 1, 20F));
        table.addCell(createCell(actDate(act.getEndDate())[0], "centerBottomBorderBottom", fontToFillIn, 2, 1, 20F));
        table.addCell(createCell("»", "leftBottomNoBorder", f5, 1, 1, 20F));
        table.addCell(createCell(actDate(act.getEndDate())[1], "centerBottomBorderBottom", fontToFillIn, 5, 1, 20F));
        table.addCell(createCell("", "rightBottomNoBorder", f14, 1, 1, 20F));
        table.addCell(createCell(actDate(act.getEndDate())[2], "centerBottomBorderBottom", fontToFillIn, 2, 1, 20F));
        table.addCell(createCell("г.", "leftBottomNoBorder", f5, 1, 1, 20F));

        addThirdStaticBlock(table);

        addLongString(act.getWorks(), table, fontToFillIn, 36);
        table.addCell(createCell("(наименование скрытых работ)",
                "centerTopNoBorder", subscript, 36, 1, 0.0F));

        table.addCell(createCell("2. Работы выполнены по проектной документации", "leftCenterNoBorder", f5, 36, 1, 20F));
        table.addCell(createCell(act.getProjectName(), "centerBorderBottom", fontToFillIn, 36, 1, 0.0F));
        table.addCell(createCell("(номер, другие реквизиты чертежа, наименование проектной и/или рабочей документации, " +
                        "сведения о лицах, осуществляющих подготовку раздела проектной и/или рабочей документации)",
                "centerTopNoBorder", subscript, 36, 1, 0.0F));

        table.addCell(createCell("3. При выполнении работ применены", "leftCenterNoBorder", f5, 15, 1, 0.0F));
        addMaterials(act.getMaterials(), table);

        table.addCell(createCell("4. Предъявлены документы, подтверждающие соответствие работ предъявляемым к ним  требованиям",
                "leftCenterNoBorder", f5, 36, 1, 0.0F));
        addLongString(act.getSubmittedDocuments(), table, fontToFillIn, 36);
        table.addCell(createCell("(исполнительные схемы и чертежи, результаты экспертиз, обследований, лабораторных " +
                        "и иных испытаний выполненных работ, проведенных в процессе строительного контроля)",
                "centerTopNoBorder", subscript, 36, 1, 0.0F));

        table.addCell(createCell("5. Даты:", "leftBottomNoBorder", f5, 3, 1, 20F));
        table.addCell(createCell("начала работ", "leftBottomNoBorder", f5, 8, 1, 20F));
        table.addCell(createCell("«", "rightBottomNoBorder", f5, 1, 1, 20F));
        table.addCell(createCell(actDate(act.getStartDate())[0], "centerBottomBorderBottom", fontToFillIn, 2, 1, 20F));
        table.addCell(createCell("»", "leftBottomNoBorder", f5, 1, 1, 20F));
        table.addCell(createCell(actDate(act.getStartDate())[1], "centerBottomBorderBottom", fontToFillIn, 4, 1, 20F));
        table.addCell(createCell("", "rightBottomNoBorder",
                f5, 1, 1, 20F));
        table.addCell(createCell(actDate(act.getStartDate())[2], "centerBottomBorderBottom",
                fontToFillIn, 3, 1, 20F));
        table.addCell(createCell("г.", "leftBottomNoBorder", f5, 13, 1, 20F));

        table.addCell(createCell("", "leftBottomNoBorder", f5, 3, 1, 0.0F));
        table.addCell(createCell("окончания работ", "leftBottomNoBorder", f5, 8, 1, 0.0F));
        table.addCell(createCell("«", "rightBottomNoBorder", f5, 1, 1, 0.0F));
        table.addCell(createCell(actDate(act.getEndDate())[0], "centerBottomBorderBottom", fontToFillIn, 2, 1, 0.0F));
        table.addCell(createCell("»", "leftBottomNoBorder", f5, 1, 1, 0.0F));
        table.addCell(createCell(actDate(act.getEndDate())[1], "centerBottomBorderBottom", fontToFillIn, 4, 1, 0.0F));
        table.addCell(createCell("", "rightBottomNoBorder", f5, 1, 1, 0.0F));
        table.addCell(createCell(actDate(act.getEndDate())[2], "centerBottomBorderBottom",
                fontToFillIn, 3, 1, 0.0F));
        table.addCell(createCell("г.", "leftBottomNoBorder", f5, 13, 1, 0.0F));

        table.addCell(createCell("6. Работы выполнены в соответствии с", "leftCenterNoBorder", f5, 36, 1, 0.0F));
        addLongString(act.getInAccordWith(), table, fontToFillIn, 36);
        table.addCell(createCell("(наименования и структурные единицы технических регламентов, иных нормативных " +
                        "правовых актов, разделы проектной и (или) рабочей документации)",
                "centerTopNoBorder", subscript, 36, 1, 0.0F));

        table.addCell(createCell("7. Разрешается  производство   последующих  работ", "leftCenterNoBorder", f5, 36, 1, 0.0F));
        addLongString(act.getNextWorks(), table, fontToFillIn, 36);
        table.addCell(createCell("(наименование работ, строительных конструкций, участков сетей инженерно-технического обеспечения)",
                "centerTopNoBorder", subscript, 36, 1, 0.0F));

        table.addCell(createCell("Дополнительные сведения", "leftCenterNoBorder", f5, 11, 1, 0.0F));
        table.addCell(createCell("н/п", "centerBottomBorderBottom", fontToFillIn, 25, 1, 0.0F));
        table.addCell(createCell("Акт составлен в  ", "leftCenterNoBorder", f5, 7, 1, 0.0F));
        table.addCell(createCell("3", "centerBottomBorderBottom", fontToFillIn, 2, 1, 0.0F));
        table.addCell(createCell("экземплярах (в случае заполнения акта на бумажном носителе).", "leftCenterNoBorder", f5, 27, 1, 0.0F));

        table.addCell(createCell("Приложения:", "leftCenterNoBorder", f5, 36, 1, 0.0F));
        addLongString(act.getSubmittedDocuments(), table, fontToFillIn, 36);

        addForthStaticBlock(table);
    }

    private void addFirstStaticBlock(PdfPTable table) {
        table.addCell(createCell("", "centerNoBorder", f10, 23, 1, 0.0F));
        table.addCell(createCell("Приказ Минстроя  №344/пр от 16.05.2023", "rightCenterNoBorder", f10, 13, 1, 0.0F));
        table.addCell(createCell("", "centerNoBorder", f10, 31, 1, 0.0F));
        table.addCell(createCell("приложение №3", "rightCenterNoBorder", f10, 5, 1, 0.0F));
        table.addCell(createCell("Объект капитального строительства", "leftCenterNoBorder", f5, 36, 1, 0.0F));
    }

    private void addSecondStaticBlock(PdfPTable table) {
        table.addCell(createCell("РФ, Краснодарский кр., г. Новороссийск, ш. Сухумское, д. 85, к. 1", "centerBorderBottom", fontToFillIn, 36, 1, 0.0F));
        table.addCell(createCell("(наименование объекта капитального строительства в соответствии с проектной документацией, почтовый или строительный адрес объекта капитального строительства)", "centerTopNoBorder", subscript, 36, 1, 0.0F));
        table.addCell(createCell("Застройщик, технический заказчик, лицо, ответственное за  эксплуатацию здания, сооружения, или региональный оператор",
                "leftCenterNoBorder", f5, 36, 1, 0.0F));
        table.addCell(createCell("АО «Черномортранснефть», ОГРН 1022302384136, ИНН 2315072242, 353911, Россия, Краснодарский край,",
                "centerBorderBottom", fontToFillIn, 36, 1, 0.0F));
        table.addCell(createCell("(фамилия, имя, отчество (последнее -  при наличии), адрес места жительства, ОРГНИП, " +
                "ИНН индивидуального предпринимателя, полное и (или) сокращенное наименование,  ", "centerTopNoBorder", subscript, 36, 1, 0.0F));
        table.addCell(createCell("г. Новороссийск, Сухумское шоссе, д.85, к.1, (8617) 60-34-51, 60-92-61, 60-92-80, Факс: (8617) 64-55-81",
                "centerBorderBottom", fontToFillIn, 36, 1, 0.0F));
        table.addCell(createCell("ОГРН, ИНН, адрес юридического лица в пределах его места нахождения, телефон или факс, " +
                "полное и (или) сокращенное наименование, ОГРН, ИНН саморегулируемой организации,", "centerTopNoBorder", subscript, 36, 1, 0.0F));
        table.addCell(createCell("Саморегулируемая организация «Союз Професиональных Строителей Южного Региона» ОГРН 1092300003400,",
                "centerBorderBottom", fontToFillIn, 36, 1, 0.0F));
        table.addCell(createCell("членом которой является указанное юридическое лицо или индивидуальный предприниматель " +
                "(за исключением случаев, когда членство в саморегулируемых организациях", "centerTopNoBorder", subscript, 36, 1, 0.0F));
        table.addCell(createCell("ИНН2310141990, 350015, Краснодарский Край, г. Краснодар, ул. Коммунаров, д. 258, тел. (факс) +7(861)2981178",
                "centerBorderBottom", fontToFillIn, 36, 1, 0.0F));
        table.addCell(createCell(" в области строительства, реконструкции, капитального ремонта объектов капитального " +
                "строительства не требуется);", "centerTopNoBorder", subscript, 36, 1, 0.0F));
        table.addCell(createCell("Лицо, осуществляющее строительство, реконструкцию, капитальный ремонт",
                "leftCenterNoBorder", f5, 36, 1, 0.0F));
        table.addCell(createCell("ООО «ЭНЕРГОМОНТАЖ» ОГРН 1157456011899, ИНН7456028407,455025,РФ,Челябинская область,",
                "centerBorderBottom", fontToFillIn, 36, 1, 0.0F));
        table.addCell(createCell("(фамилия, имя, отчество (последнее - при наличии), адрес места жительства, ОГРНИП, " +
                "ИНН индивидуального предпринимателя, полное и (или) сокращенное наименование,", "centerTopNoBorder", subscript, 36, 1, 0.0F));
        table.addCell(createCell("г.о. Магнитогорский, г. Магнитогорск, ул. Лесопарковая, д. 93, к.3, пом. 6, тел +7 (951)244-35-65, +7(3519)33-01-04",
                "centerBorderBottom", fontToFillIn, 36, 1, 0.0F));
        table.addCell(createCell("ОГРН, ИНН, адрес юридического лица в пределах его места нахождения, телефон или факс, " +
                "полное и (или) сокращенное наименование, ОГРН, ИНН саморегулируемой организации,", "centerTopNoBorder", subscript, 36, 1, 0.0F));
        table.addCell(createCell("«Союз строительных компаний Урала и Сибири» ОГРН 1087400001897 ИНН 7453198672, 454092, Челябинская область,",
                "centerBorderBottom", fontToFillIn, 36, 1, 0.0F));
        table.addCell(createCell("членом которой является указанное юридическое лицо или индивидуальный предприниматель " +
                "(за исключением случаев, когда членство в саморегулируемых организациях", "centerTopNoBorder", subscript, 36, 1, 0.0F));
        table.addCell(createCell("г. Челябинск, ул. Елькина, д. 84, тел. (факс) +7 351 280-41-14",
                "centerBorderBottom", fontToFillIn, 36, 1, 0.0F));
        table.addCell(createCell(" в области строительства, реконструкции, капитального ремонта объектов капитального" +
                " строительства не требуется)", "centerTopNoBorder", subscript, 36, 1, 0.0F));
        table.addCell(createCell("Лицо, осуществляющее подготовку проектной документации",
                "leftCenterNoBorder", f5, 36, 1, 0.0F));
        table.addCell(createCell("Проектно-сметное бюро, АО «Черномортранснефть», ОГРН 1022302384136 ИНН 2315072242, РФ,",
                "centerBorderBottom", fontToFillIn, 36, 1, 0.0F));
        table.addCell(createCell("(фамилия, имя, отчество (последнее - при наличии), адрес места жительства, ОГРНИП, " +
                "ИНН индивидуального предпринимателя, полное и (или) сокращенное наименование,", "centerTopNoBorder", subscript, 36, 1, 0.0F));
        table.addCell(createCell("Краснодарский край, г. Новороссийск, Сухумское шоссе, д.85, к.1, (8617) 60-34-51, 60-92-61, Факс: (8617) 64-55-81",
                "centerBorderBottom", fontToFillIn, 36, 1, 0.0F));
        table.addCell(createCell("ОГРН, ИНН, адрес юридического лица в пределах его места нахождения, телефон или факс, " +
                "полное и (или) сокращенное наименование, ОГРН, ИНН саморегулируемой организации,", "centerTopNoBorder", subscript, 36, 1, 0.0F));
        table.addCell(createCell("Саморегулируемая организация «Союз Професиональных Строителей Южного Региона» ОГРН 1092300003400,",
                "centerBorderBottom", fontToFillIn, 36, 1, 0.0F));
        table.addCell(createCell("членом которой является указанное юридическое лицо или индивидуальный предприниматель " +
                "(за исключением случаев, когда членство в саморегулируемых организациях", "centerTopNoBorder", subscript, 36, 1, 0.0F));
        table.addCell(createCell("ИНН2310141990, 350015, Краснодарский Край, г. Краснодар, ул. Коммунаров, д. 258, тел. (факс) +7(861)2981178",
                "centerBorderBottom", fontToFillIn, 36, 1, 0.0F));
        table.addCell(createCell(" в области строительства, реконструкции, капитального ремонта объектов капитального" +
                " строительства не требуется)", "centerTopNoBorder", subscript, 36, 1, 0.0F));
        table.addCell(createCell("АКТ", "centerBottomNoBorder", f13, 36, 1, 30F));
        table.addCell(createCell("освидетельствования скрытых работ", "centerNoBorder", f13, 36, 1, 0.0F));
    }

    private void addThirdStaticBlock(PdfPTable table) {
        table.addCell(createCell("", "centerNoBorder", f5, 23, 1, 20F));
        table.addCell(createCell("(дата составления акта)", "centerTopNoBorder", subscript, 13, 1, 20F));

        table.addCell(createCell("Представитель застройщика, технического заказчика, лица, ответственного за " +
                        "эксплуатацию здания, сооружения, или регионального оператора по вопросам строительного контроля",
                "leftTopNoBorder", f5, 36, 1, 0.0F));
        table.addCell(createCell("Ведущий инженер ОКС ПК «Шесхарис» А.А. Челебиев, приказ № 155 от 19.02.2024 г.",
                "centerBorderBottom", fontToFillIn, 36, 1, 0.0F));
        table.addCell(createCell("(должность (при наличии), фамилия, инициалы, идентификационный номер в национальном " +
                "реестре специалистов в области строительства (за исключением случаев,", "centerTopNoBorder", subscript, 36, 1, 0.0F));
        table.addCell(createCell("АО «Черномортранснефть», ОГРН 1022302384136, ИНН 2315072242, почтовый адрес: 353911, Россия,",
                "centerBorderBottom", fontToFillIn, 36, 1, 0.0F));
        table.addCell(createCell(" когда членство в саморегулируемых организациях в области строительства, реконструкции, " +
                "капитального ремонта объектов капитального строительства не требуется),", "centerTopNoBorder", subscript, 36, 1, 0.0F));
        table.addCell(createCell("Краснодарский край, г. Новороссийск, Шесхарис-11, тел. +7 (8617) 645740, факс +7 (8617) 645581",
                "centerBorderBottom", fontToFillIn, 36, 1, 0.0F));
        table.addCell(createCell(" реквизиты распорядительного документа, подтверждающего полномочия, с указанием полного " +
                "и (или) сокращенного наименования, ОГРН, ИНН, адреса юридического лица в пределах его места" +
                " нахождения (в случае осуществления строительного контроля на основании договора с застройщиком или " +
                "техническим заказчиком), фамилии, имени, отчества (последнее - при наличии), адреса места " +
                "жительства, ОГРНИП, ИНН индивидуального предпринимателя (в случае осуществления строительного " +
                "контроля на основании договора с застройщиком или техническим заказчиком)", "centerTopNoBorder", subscript, 36, 1, 0.0F));
        table.addCell(createCell("Представитель лица, осуществляющего строительство, реконструкцию, капитальный ремонт",
                "leftTopNoBorder", f5, 36, 1, 0.0F));
        table.addCell(createCell("Руководитель работ ООО «ЭНЕРГОМОНТАЖ» А.Е. Трифонов, приказ №696/16 от 16.07.2024 г.",
                "centerBorderBottom", fontToFillIn, 36, 1, 0.0F));
        table.addCell(createCell("(должность (при наличии), фамилия, инициалы, реквизиты распорядительного документа, " +
                "подтверждающего полномочия)", "centerTopNoBorder", subscript, 36, 1, 0.0F));
        table.addCell(createCell("Представитель лица, осуществляющего строительство, реконструкцию, капитальный ремонт, " +
                "по вопросам строительного контроля", "leftTopNoBorder", f5, 36, 1, 0.0F));
        table.addCell(createCell("Начальник СКК ООО «Энергомонтаж» Л.С. Попова, приказ №176/14.295.24-ЧТН-2024 от 21.06.2024 г.",
                "centerBorderBottom", fontToFillIn, 36, 1, 0.0F));
        table.addCell(createCell("(должность (при наличии), фамилия, инициалы, идентификационный номер" +
                        " в национальном реестре специалистов в области строительства (за исключением случаев, когда членство в " +
                        "саморегулируемых организациях в области строительства, реконструкции, капитального ремонта объектов капитального " +
                        "строительства не требуется), реквизиты распорядительного документа, подтверждающего полномочия)",
                "centerTopNoBorder", subscript, 36, 1, 0.0F));
        table.addCell(createCell("Представитель лица, осуществляющего подготовку проектной документации (в случае привлечения " +
                        "застройщиком лица, осуществляющего подготовку проектной документации, для проверки соответствия выполняемых работ " +
                        "проектной документации согласно части 2 статьи 53 Градостроительного кодекса Российской Федерации)",
                "leftTopNoBorder", f5, 36, 1, 0.0F));
        table.addCell(createCell("н/п", "centerBorderBottom", fontToFillIn, 36, 1, 0.0F));
        table.addCell(createCell("(должность (при наличии), фамилия, инициалы, реквизиты распорядительного документа, " +
                "подтверждающего полномочия, с указанием полного и (или) сокращенного наименования, ОГРН, ИНН, адреса юридического лица " +
                "в пределах его места нахождения, фамилии, имени, отчества (последнее - при наличии), адреса места жительства, ОГРНИП, " +
                "ИНН индивиуального предпринимателя)", "centerTopNoBorder", subscript, 36, 1, 0.0F));
        table.addCell(createCell("Представитель лица, выполнившего работы, подлежащие освидетельствованию (в случае " +
                "выполнения работ по договорам о строительстве, реконструкции, капитальном ремонте объектов капитального строительства, " +
                "заключенным с иными лицами)", "leftTopNoBorder", f5, 36, 1, 0.0F));
        table.addCell(createCell("н/п", "centerBorderBottom", fontToFillIn, 36, 1, 0.0F));
        table.addCell(createCell("(должность (при наличии), фамилия, инициалы, реквизиты распорядительного документа, " +
                "подтверждающего полномочия, с указанием полного и (или) сокращенного наименования, ОГРН, ИНН, адреса юридического лица " +
                "в пределах его места нахождения, фамилии, имени, отчества (последнее - при наличии), адреса места жительства, ОГРНИП, " +
                "ИНН индивиуального предпринимателя)", "centerTopNoBorder", subscript, 36, 1, 0.0F));
        table.addCell(createCell("произвели осмотр работ, выполненных", "leftTopNoBorder", f5, 36, 1, 0.0F));
        table.addCell(createCell("ООО «ЭНЕРГОМОНТАЖ»", "centerBorderBottom", fontToFillIn, 36, 1, 0.0F));
        table.addCell(createCell("(полное и (или) сокращенное наименование или фамилия, имя, отчество (последнее - при наличии) " +
                "лица,  выполнившего работы, подлежащие освидетельствованию)", "centerTopNoBorder", subscript, 36, 1, 0.0F));
        table.addCell(createCell("и составили настоящий акт о нижеследующем:", "leftTopNoBorder", f5, 36, 1, 0.0F));
        table.addCell(createCell("1. К освидетельствованию предъявлены следующие работы:", "leftTopNoBorder", f5, 36, 1, 0.0F));
    }

    private void addForthStaticBlock(PdfPTable table) {
        table.addCell(createCell("(исполнительные схемы и чертежи, результаты экспертиз, обследований, лабораторных и иных испытаний)",
                "centerTopNoBorder", subscript, 36, 1, 0.0F));

        table.addCell(createCell("Представитель застройщика, технического заказчика, лица, ответственного за " +
                        "эксплуатацию здания, сооружения, или регионального оператора по вопросам строительного контроля", "leftCenterNoBorder",
                f5, 36, 1, 0.0F));
        table.addCell(createCell("Челебиев А.А.", "centerBottomBorderBottom", fontToFillIn, 15, 1, 0.0F));
        addSubscripts(table);

        table.addCell(createCell("Представитель лица, осуществляющего строительство, реконструкцию, капитальный ремонт", "leftCenterNoBorder",
                f5, 36, 1, 0.0F));
        table.addCell(createCell("Трифонов А.Е.", "centerBottomBorderBottom", fontToFillIn, 15, 1, 0.0F));
        addSubscripts(table);

        table.addCell(createCell("Представитель лица, осуществляющего строительство, реконструкцию, капитальный ремонт, " +
                "по вопросам строительного контроля", "leftCenterNoBorder", f5, 36, 1, 0.0F));
        table.addCell(createCell("Попова Л.С.", "centerBottomBorderBottom", fontToFillIn, 15, 1, 0.0F));
        addSubscripts(table);

        table.addCell(createCell("Представитель лица, осуществляющего подготовку проектной документации (в случае привлечения " +
                        "застройщиком лица, осуществляющего подготовку проектной документации, для проверки соответствия " +
                        "выполняемых работ проектной документации  согласно части 2 статьи 53 Градостроительного кодекса Российской Федерации)",
                "leftCenterNoBorder", f5, 36, 1, 0.0F));
        table.addCell(createCell("н/п", "centerBottomBorderBottom", fontToFillIn, 15, 1, 0.0F));
        addSubscripts(table);

        table.addCell(createCell("Представитель лица, выполнившего работы, подлежащие освидетельствованию (в случае " +
                "выполнения работ по договорам о строительстве, реконструкции, капитальном ремонте объектов капитального " +
                "строительства, заключенным с иными лицами)", "leftCenterNoBorder", f5, 36, 1, 0.0F));
        table.addCell(createCell("н/п", "centerBottomBorderBottom", fontToFillIn, 15, 1, 0.0F));
        addSubscripts(table);
    }

    private void addSubscripts(PdfPTable table) {
        table.addCell(createCell("", "leftCenterNoBorder", f5, 12, 1, 0.0F));
        table.addCell(createCell("", "centerBottomBorderBottom", fontToFillIn, 9, 1, 0.0F));
        table.addCell(createCell("(фамилия, инициалы)", "centerTopNoBorder", subscript, 15, 1, 0.0F));
        table.addCell(createCell("", "centerTopNoBorder", subscript, 12, 1, 0.0F));
        table.addCell(createCell("(подпись)", "centerTopNoBorder", subscript, 9, 1, 0.0F));
    }

    // entrance control acts

    private void addControlTableData(PdfPTable controlTable, EntranceControl control, ActResponseDto act) {
        String controlDate = control.getDate().toString();
        String[] controlDateList = controlDate.split("-");
        controlDate = controlDateList[2] + " " + getMonth(controlDateList[1]) + " " + controlDateList[0] + " г.";

        controlTable.addCell(createCell("ООО «ЭНЕРГОМОНТАЖ»", "centerBorderBottom", fontToFillInControl, 9, 1, 0.0F));
        controlTable.addCell(createCell("(наименование строительной организации)", "centerTopNoBorder", subscript, 9, 1, 0.0F));
        controlTable.addCell(createCell(clearProjectNameForControls(act.getProjectName(), 1), "centerBorderBottom", fontToFillInControl, 9, 1, 0.0F));
        controlTable.addCell(createCell("(наименование объекта)", "centerTopNoBorder", subscript, 9, 1, 0.0F));
        controlTable.addCell(createCell("АКТ №", "rightBottomNoBorder", f1, 4, 1, 30F));
        controlTable.addCell(createCell(act.getActNumber(), "centerBottomBorderBottom", fontToFillInControl, 2, 1, 30F));
        controlTable.addCell(createCell("", "centerNoBorder", fontToFillInControl, 3, 1, 30F));
        controlTable.addCell(createCell("результатов входного контроля МТР и оборудования", "centerBottomNoBorder", f1, 9, 1, 30F));
        addLongString(control.getMaterials(), controlTable, fontToFillInControl, 9);
        controlTable.addCell(createCell("((наименование МТР)", "centerTopNoBorder", subscript, 9, 1, 0.0F));
        controlTable.addCell(createCell("от", "rightBottomNoBorder", f1, 4, 1, 0.0F));
        controlTable.addCell(createCell(controlDate, "centerBottomBorderBottom", fontToFillInControl, 2, 1, 0.0F));
        controlTable.addCell(createCell("", "centerNoBorder", fontToFillInControl, 3, 1, 0.0F));
        controlTable.addCell(createCell("Составлен представителями:", "leftCenterNoBorder", f1, 9, 1, 0.0F));
        controlTable.addCell(createCell("субподрядной организации", "leftCenterNoBorder", f1, 2, 1, 0.0F));
        controlTable.addCell(createCell("Руководитель работ ООО «ЭНЕРГОМОНТАЖ» А.Е. Трифонов", "centerBorderBottom", fontToFillInControl, 7, 1, 0.0F));
        controlTable.addCell(createCell("", "centerNoBorder", fontToFillInControl, 2, 1, 0.0F));
        controlTable.addCell(createCell("(должность, организация, ФИО)", "centerTopNoBorder", subscript, 7, 1, 0.0F));
        controlTable.addCell(createCell("строительного контроля подрядчика", "leftCenterNoBorder", f1, 3, 1, 0.0F));
        controlTable.addCell(createCell("Начальник отдела контроля качества", "centerBorderBottom", fontToFillInControl, 6, 1, 0.0F));
        controlTable.addCell(createCell("", "centerNoBorder", fontToFillInControl, 3, 1, 0.0F));
        controlTable.addCell(createCell("(должность, организация, ФИО)", "centerTopNoBorder", subscript, 6, 1, 0.0F));
        controlTable.addCell(createCell("ООО «ЭНЕРГОМОНТАЖ» Попова Л.С.", "centerBorderBottom", fontToFillInControl, 9, 1, 0.0F));
        controlTable.addCell(createCell("строительного контроля застройщика или технического заказчика", "leftCenterNoBorder", f1, 9, 1, 0.0F));
        controlTable.addCell(createCell("н/п", "centerBorderBottom", fontToFillInControl, 9, 1, 0.0F));
        controlTable.addCell(createCell("(должность, организация, ФИО)", "centerTopNoBorder", subscript, 9, 1, 0.0F));
        controlTable.addCell(createCell("застройщика ", "leftCenterNoBorder", f1, 9, 1, 0.0F));
        controlTable.addCell(createCell("или технического заказчика ", "leftCenterNoBorder", f1, 2, 1, 0.0F));
        controlTable.addCell(createCell("Ведущий инженер ОКС ПК «Шесхарис» А.А. Челебиев", "centerBorderBottom", fontToFillInControl, 7, 1, 0.0F));
        controlTable.addCell(createCell("", "leftCenterNoBorder", f1, 2, 1, 0.0F));
        controlTable.addCell(createCell("(должность, организация, ФИО)", "centerTopNoBorder", subscript, 7, 1, 0.0F));
        controlTable.addCell(createCell("в том, что произведен", "leftCenterNoBorder", f1, 2, 1, 0.0F));
        controlTable.addCell(createCell("выборочный", "centerBorderBottom", fontToFillInControl, 2, 1, 0.0F));
        controlTable.addCell(createCell("осмотр МТР и оборудования", "leftCenterNoBorder", f1, 5, 1, 0.0F));
        controlTable.addCell(createCell("", "leftCenterNoBorder", f1, 2, 1, 0.0F));
        controlTable.addCell(createCell("(сплошной, выборочный)", "centerTopNoBorder", subscript, 2, 1, 0.0F));
        controlTable.addCell(createCell("", "leftCenterNoBorder", f1, 5, 1, 0.0F));
        addLongString(control.getMaterials(), controlTable, fontToFillInControl, 9);
        controlTable.addCell(createCell("(наименование)", "centerTopNoBorder", subscript, 9, 1, 0.0F));
        controlTable.addCell(createCell("предназначенных проектной документацией", "leftCenterNoBorder", f1, 4, 1, 0.0F));
        controlTable.addCell(createCell(clearProjectNameForControls(act.getProjectName(), 2), "centerBorderBottom", fontToFillInControl, 5, 1, 0.0F));
        controlTable.addCell(createCell("", "leftCenterNoBorder", f1, 4, 1, 0.0F));
        controlTable.addCell(createCell("(шифр, раздел, номер изменения проектной документации)", "centerTopNoBorder", subscript, 5, 1, 0.0F));
        controlTable.addCell(createCell("для строительства на участке", "leftCenterNoBorder", f1, 9, 1, 0.0F));
        addLongString(control.getSubObjectName(), controlTable, fontToFillInControl, 9);
        controlTable.addCell(createCell("(участок линейной части (км/ПК), подобъект НПС/ЛПДС)", "centerTopNoBorder", subscript, 9, 1, 0.0F));
        controlTable.addCell(createCell("1. Осмотром геометрических размеров, маркировки МТР и оборудования", "leftCenterNoBorder", f1, 9, 1, 0.0F));
        addLongString(control.getMaterials(), controlTable, fontToFillInControl, 9);
        controlTable.addCell(createCell("(наименование, заводской номер)", "centerTopNoBorder", subscript, 9, 1, 0.0F));
        controlTable.addCell(createCell("сопроводительной документации", "leftCenterNoBorder", f1, 9, 1, 0.0F));
        addLongString(control.getDocuments(), controlTable, fontToFillInControl, 9);
        controlTable.addCell(createCell("(паспорта, сертификаты)", "centerTopNoBorder", subscript, 9, 1, 0.0F));
        controlTable.addCell(createCell("установлено, что данный МТР и оборудование по своим техническим параметрам", "leftCenterNoBorder", f1, 9, 1, 0.0F));
        controlTable.addCell(createCell("Внешний вид, количество", "centerBorderBottom", fontToFillInControl, 9, 1, 0.0F));
        controlTable.addCell(createCell("(контролируемые параметры)", "centerTopNoBorder", subscript, 9, 1, 0.0F));
        controlTable.addCell(createCell("номеру технических условий", "leftCenterNoBorder", f1, 3, 1, 0.0F));
        controlTable.addCell(createCell(control.getStandard(), "centerBorderBottom", fontToFillInControl, 6, 1, 0.0F));
        controlTable.addCell(createCell("", "leftCenterNoBorder", f1, 3, 1, 0.0F));
        controlTable.addCell(createCell("(контролируемые параметры)", "centerTopNoBorder", subscript, 6, 1, 0.0F));
        controlTable.addCell(createCell("техническим характеристикам", "leftCenterNoBorder", f1, 3, 1, 0.0F));
        controlTable.addCell(createCell("по данным сопроводительной документации", "centerBorderBottom", fontToFillInControl, 6, 1, 0.0F));
        controlTable.addCell(createCell("", "leftCenterNoBorder", f1, 3, 1, 0.0F));
        controlTable.addCell(createCell("(по данным сопроводительной документации, результатам испытаний)", "centerTopNoBorder", subscript, 6, 1, 0.0F));
        controlTable.addCell(createCell("соответствует", "centerBorderBottom", fontToFillInControl, 6, 1, 0.0F));
        controlTable.addCell(createCell("проектной документации.", "leftCenterNoBorder", f1, 3, 1, 0.0F));
        controlTable.addCell(createCell("(соответствует/не соответствует)", "centerTopNoBorder", subscript, 6, 1, 0.0F));
        controlTable.addCell(createCell("", "leftCenterNoBorder", f1, 3, 1, 0.0F));
        controlTable.addCell(createCell("2. Сопроводительная документация на МТР и оборудование", "leftCenterNoBorder", f1, 9, 1, 0.0F));
        addLongString(control.getDocuments(), controlTable, fontToFillInControl, 9);
        controlTable.addCell(createCell("(паспорта, сертификаты)", "centerTopNoBorder", subscript, 9, 1, 0.0F));
        controlTable.addCell(createCell("имеется в полном комплекте.", "leftCenterNoBorder", f1, 9, 1, 0.0F));
        controlTable.addCell(createCell("3. МТР и оборудование", "leftCenterNoBorder", f1, 2, 1, 0.0F));
        controlTable.addCell(createCell("не находится", "centerBorderBottom", fontToFillInControl, 2, 1, 0.0F));
        controlTable.addCell(createCell("в Перечне основных видов МТР и оборудования.", "leftCenterNoBorder", f1, 5, 1, 0.0F));
        controlTable.addCell(createCell("", "leftCenterNoBorder", f1, 2, 1, 0.0F));
        controlTable.addCell(createCell("(находится/не находится)", "centerTopNoBorder", subscript, 2, 1, 0.0F));
        controlTable.addCell(createCell("", "leftCenterNoBorder", f1, 5, 1, 0.0F));
        controlTable.addCell(createCell("4. Техническая документация на МТР и оборудование ", "leftCenterNoBorder", f1, 5, 1, 0.0F));
        controlTable.addCell(createCell("отсутствует в Реестре", "centerBorderBottom", fontToFillInControl, 4, 1, 0.0F));
        controlTable.addCell(createCell("", "leftCenterNoBorder", f1, 5, 1, 0.0F));
        controlTable.addCell(createCell("(номер учетной записи в Реестре/отсутствует в Реестре)", "centerTopNoBorder", subscript, 4, 1, 0.0F));
        controlTable.addCell(createCell("5. Дополнительно отмечено следующее", "leftCenterNoBorder", f1, 4, 1, 0.0F));
        controlTable.addCell(createCell("н/п", "centerBorderBottom", fontToFillInControl, 5, 1, 0.0F));
        controlTable.addCell(createCell("", "leftCenterNoBorder", f1, 4, 1, 0.0F));
        controlTable.addCell(createCell("(заполняется при необходимости)", "centerTopNoBorder", subscript, 5, 1, 0.0F));
        controlTable.addCell(createCell("Представитель субподрядной", "leftBottomNoBorder", f1, 9, 1, 30F));
        controlTable.addCell(createCell("строительной организации", "leftCenterNoBorder", f1, 9, 1, 0.0F));
        controlTable.addCell(createCell("ООО «ЭНЕРГОМОНТАЖ» А.Е. Трифонов", "centerBorderBottom", fontToFillInControl, 4, 1, 0.0F));
        addControlSigns(controlTable);
        controlTable.addCell(createCell("Представитель службы", "leftCenterNoBorder", f1, 9, 1, 0.0F));
        controlTable.addCell(createCell("строительного контроля", "leftCenterNoBorder", f1, 9, 1, 0.0F));
        controlTable.addCell(createCell("подрядчика", "leftCenterNoBorder", f1, 9, 1, 0.0F));
        controlTable.addCell(createCell("ООО «ЭНЕРГОМОНТАЖ» Л.С. Попова", "centerBorderBottom", fontToFillInControl, 4, 1, 0.0F));
        addControlSigns(controlTable);
        controlTable.addCell(createCell("Представитель службы", "leftCenterNoBorder", f1, 9, 1, 0.0F));
        controlTable.addCell(createCell("строительного контроля", "leftCenterNoBorder", f1, 9, 1, 0.0F));
        controlTable.addCell(createCell("застройщика или", "leftCenterNoBorder", f1, 9, 1, 0.0F));
        controlTable.addCell(createCell("технического заказчика", "leftCenterNoBorder", f1, 9, 1, 0.0F));
        controlTable.addCell(createCell("", "centerBorderBottom", fontToFillInControl, 4, 1, 0.0F));
        addControlSigns(controlTable);
        controlTable.addCell(createCell("Представитель застройщика", "leftCenterNoBorder", f1, 9, 1, 0.0F));
        controlTable.addCell(createCell("или технического заказчика", "leftCenterNoBorder", f1, 9, 1, 0.0F));
        controlTable.addCell(createCell("ПК «Шесхарис» А.А. Челебиев", "centerBorderBottom", fontToFillInControl, 4, 1, 0.0F));
        addControlSigns(controlTable);
    }

    void addControlSigns(PdfPTable controlTable) {
        controlTable.addCell(createCell("", "leftCenterNoBorder", f1, 1, 1, 0.0F));
        controlTable.addCell(createCell("", "centerBorderBottom", fontToFillInControl, 3, 1, 0.0F));
        controlTable.addCell(createCell("М.П.", "leftCenterNoBorder", f1, 1, 1, 0.0F));
        controlTable.addCell(createCell("(организация, ФИО)", "centerTopNoBorder", subscript, 4, 1, 0.0F));
        controlTable.addCell(createCell("", "centerTopNoBorder", subscript, 1, 1, 0.0F));
        controlTable.addCell(createCell("(подпись)", "centerTopNoBorder", subscript, 2, 1, 0.0F));
        controlTable.addCell(createCell("(дата)", "centerTopNoBorder", subscript, 1, 1, 0.0F));
        controlTable.addCell(createCell("", "centerTopNoBorder", subscript, 1, 1, 0.0F));
    }

    // utils
    // --------------------------------------------------------------------------------------------------------------------------------
    private PdfPCell createCell(String text, String position, Font font, int numberOfColumns, int numberOfRows, float cellHeight) {
        Paragraph paragraph = new Paragraph(text, font);
        PdfPCell cell = new PdfPCell(paragraph);

        if (numberOfColumns > 1) {
            cell.setColspan(numberOfColumns);
        }

        if (numberOfRows > 1) {
            cell.setRowspan(numberOfRows);
        }

        if (cellHeight > 0.0F) {
            cell.setFixedHeight(cellHeight);
        }


        switch (position) {
            case "centerBorder":
                cellStyler.createCellStyleHorizontalCenterBorder(cell);
                break;
            case "centerNoBorder":
                cellStyler.createCellStyleHorizontalCenterAndVerticalCenter(cell);
                break;
            case "centerBottomNoBorder":
                cellStyler.createCellStyleHorizontalCenterAndVerticalBottomNoBorder(cell);
                break;
            case "leftTopNoBorder":
                cellStyler.createCellStyleHorizontalLeftAndVerticalTopNoBorder(cell);
                break;
            case "leftCenterNoBorder":
                cellStyler.createCellStyleHorizontalLeftAndVerticalCenterNoBorder(cell);
                break;
            case "leftBottomNoBorder":
                cellStyler.createCellStyleHorizontalLeftAndVerticalBottomNoBorder(cell);
                break;
            case "emptyCellBottomBorder":
                cellStyler.createCellStyleBottomBorder(cell);
                break;
            case "centerTopNoBorder":
                cellStyler.createCellStyleHorizontalCenterAndVerticalTopNoBorder(cell);
                break;
            case "rightBottomNoBorder":
                cellStyler.createCellStyleHorizontalRightAndVerticalBottomNoBorder(cell);
                break;
            case "rightCenterNoBorder":
                cellStyler.createCellStyleHorizontalRightAndVerticalCenterNoBorder(cell);
                break;
            case "rightTopNoBorder":
                cellStyler.createCellStyleHorizontalRightAndVerticalTopNoBorder(cell);
                break;
            case "centerBorderBottom":
                cellStyler.createCellStyleHorizontalCenterAndVerticalCenterBottomBorder(cell);
                break;
            case "centerBottomBorderBottom":
                cellStyler.createCellStyleHorizontalCenterAndVerticalBottomBottomBorder(cell);
                break;
            case "leftBottomBorderBottom":
                cellStyler.createCellStyleHorizontalLeftAndVerticalBottomBottomBorder(cell);
                break;
        }

        return cell;
    }

    @Override
    public void mergeUsingIText(List<PdfReader> pdfReaders, String path) throws IOException, DocumentException {

        Document document = new Document();

        FileOutputStream fos = new FileOutputStream(path);
        PdfWriter writer = PdfWriter.getInstance(document, fos);

        document.open();

        PdfContentByte directContent = writer.getDirectContent();

        PdfImportedPage pdfImportedPage;

        for (PdfReader pdfReader : pdfReaders) {
            int currentPdfReaderPage = 1;
            while (currentPdfReaderPage <= pdfReader.getNumberOfPages()) {
                document.newPage();
                pdfImportedPage = writer.getImportedPage(pdfReader, currentPdfReaderPage);
                directContent.addTemplate(pdfImportedPage, 0, 0);
                currentPdfReaderPage++;
            }
        }
        fos.flush();
        document.close();
        fos.close();
        log.info("Объединение закончено");
    }

    public int numberOfPages(String source) throws IOException {
        String path = "";
        switch (source) {
            case "registry":
                path = REGISTRY_PATH;
                break;
            case "workLog":
                path = WORK_LOG_PATH;
                break;
            case "entranceControlLog":
                path = ENTRANCE_CONTROL_PATH;
                break;
            case "registryTemp":
                path = REGISTRY_TEMP_PATH;
                break;
        }

        PdfReader reader = new PdfReader(path);
        int pages = reader.getNumberOfPages();
        reader.close();

        if (pages % 2 == 0) {
            pages = pages / 2;
        } else {
            pages = pages / 2 + 1;
        }

        return pages;
    }

    private void deleteFile(Path path) {
        try {
             Files.deleteIfExists(path);
            log.info("Файл {} удалён", path.getFileName());
        } catch (IOException e) {
            log.info("Сработало исключение {}", e.getMessage());
        }
    }

    public LocalDate jsDateToLocalDate(String date) {
        String[] arr = date.split(" ");

        int year = Integer.parseInt(arr[3]);
        int day = Integer.parseInt(arr[2]);
        String stringMonth = arr[1];

        Integer[] values = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12};
        String[] keys = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};

        Map<String, Integer> monthMap = new HashMap<>();

        for (int i = 0; i < keys.length; i++) {
            monthMap.put(keys[i], values[i]);
        }

        int month = monthMap.get(stringMonth);

        return LocalDate.of(year, month, day);
    }

    public String[] actDate(String date) {
        String[] endDateList = date.split("-");
        String day = endDateList[0];
        String month = getMonth(endDateList[1]);
        String year = endDateList[2];

        return new String[]{day, month, year};
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

    private void addLongString(String works, PdfPTable table, Font font, int numberOfColumns) {
        int currentLength = 118;

        if (font == fontToFillInControl) {
            currentLength = 98;
        }

        while (works.length() >= currentLength) {
            String worksRow = works.substring(0, currentLength - 1);
            int lastSpace = worksRow.lastIndexOf(" ");
            worksRow = worksRow.substring(0, lastSpace);
            table.addCell(createCell(worksRow, "centerBorderBottom", font, numberOfColumns, 1, 0.0F));
            works = works.replace(worksRow, "");
        }
        table.addCell(createCell(works, "centerBorderBottom", font, numberOfColumns, 1, 0.0F));
    }

    private void addMaterials(String materials, PdfPTable table) {
        if (materials.length() < 60) {
            table.addCell(createCell(materials, "centerBorderBottom", fontToFillIn, 21, 1, 0.0F));
            table.addCell(createCell("", "leftCenterNoBorder", f5, 15, 1, 0.0F));
            table.addCell(createCell("(наименования строительных  материалов (изделий),", "centerTopNoBorder",
                    subscript, 21, 1, 0.0F));
            table.addCell(createCell("н/п", "centerBorderBottom", fontToFillIn, 36, 1, 0.0F));
        } else {
            String materialsRow = materials.substring(0, 60);
            int lastSpace = materialsRow.lastIndexOf(" ");
            materialsRow = materialsRow.substring(0, lastSpace);
            table.addCell(createCell(materialsRow, "centerBorderBottom", fontToFillIn, 21, 1, 0.0F));

            table.addCell(createCell("", "leftCenterNoBorder", f5, 15, 1, 0.0F));
            table.addCell(createCell("(наименования строительных  материалов (изделий),", "centerTopNoBorder",
                    subscript, 21, 1, 0.0F));
            materials = materials.replace(materialsRow, "");
            addLongString(materials, table, fontToFillIn, 36);
        }
        table.addCell(createCell("реквизиты сертификатов и (или) других документов, подтверждающих их качество и " +
                        "безопасность, в случае если необходимо указывать более 5 документов, указывается ссылка на " +
                        "их реестр, который является неотъемлемой частью акта)", "centerTopNoBorder",
                subscript, 36, 1, 0.0F));
    }

    private String clearProjectNameForControls(String projectName, int choice) {
        String[] split = projectName.split("\\.");
        if (choice == 1) {
            projectName = split[0] + "." + split[1];
        } else {
            projectName = split[2];
        }

        return projectName;
    }
}

