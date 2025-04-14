package com.act.pdf.service;

import com.act.registry.model.Registry;
import com.act.registry.repository.RegistryRepository;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfWriter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.FileOutputStream;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class PdfServiceImplementation implements PdfService {

    public static final String FONT = "./src/main/resources/fonts/timesnewromanpsmt.ttf";
    private final Font f1 = FontFactory.getFont(FONT, "cp1251", true, 11);
    private final Font f2 = FontFactory.getFont(FONT, "cp1251", true, 12, Font.BOLD);
    private final Font f3 = FontFactory.getFont(FONT, "cp1251", true, 9, Font.ITALIC);
    private final RegistryRepository registryRepository;
    private final PdfCellStyler cellStyler = new PdfCellStyler();

    @Override
    public void exportToPdf(int monthId) throws IOException, DocumentException {
        String pdfFilePath = "C:\\Users\\PC\\Desktop\\work\\pdfFile.pdf";

        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream(pdfFilePath));
        document.open();

        PdfPTable table = new PdfPTable(6);
        table.setWidthPercentage(105);

        table.setTotalWidth(500f);
        float[] widths = new float[]{20f, 200f, 90f, 78f, 52f, 60f};
        table.setWidths(widths);

        List<Registry> registries = registryRepository.findAllByOrderByAddingTimeAsc(monthId);

        addTableData(table, registries);

        document.add(table);

        document.close();


        log.info("Export to PDF successful");

//        log.info()
    }

    @Override
    public void addTableData(PdfPTable table, List<Registry> registries) {
        addPdfHeader(table);
        addTableHeader(table);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

        for (Registry registry : registries) {
            String documentDate = " от " + registry.getDocumentDate().format(formatter) + "г.";

            String documentNumber = registry.getDocumentNumber();

            table.addCell(createCell(String.valueOf(registry.getRowNumber()), "centerBorder", f1, 1, 0.0F));
            table.addCell(createCell(String.valueOf(registry.getDocumentName()), "centerBorder", f1, 1, 0.0F));
            table.addCell(createCell(documentNumber + documentDate, "centerBorder", f1, 1, 0.0F));
            table.addCell(createCell(registry.getDocumentAuthor(), "centerBorder", f1, 1, 0.0F));
            table.addCell(createCell(String.valueOf(registry.getNumberOfSheets()), "centerBorder", f1, 1, 0.0F));
            table.addCell(createCell(String.valueOf(registry.getListInOrder()), "centerBorder", f1, 1, 0.0F));
        }

        addPdfFooter(table);
    }

    private void addPdfHeader(PdfPTable table) {

        table.addCell(createCell("", "centerNoBorder", f1, 3, 0.0F));
        table.addCell(createCell("Форма", "leftCenterNoBorder", f1, 1, 0.0F));
        table.addCell(createCell("№1.2", "leftCenterNoBorder", f1, 2, 0.0F));
//      2 строка
        table.addCell(createCell("Заказчик: АО «Черномортранснефть»", "leftCenterNoBorder", f1, 3, 0.0F));
        table.addCell(createCell("Основание", "leftCenterNoBorder", f1, 1, 0.0F));
        table.addCell(createCell("ВСН 012-88 (часть II)", "leftCenterNoBorder", f1, 2, 0.0F));
//      3 строка
        table.addCell(createCell("Подрядчик: ООО «Энергомонтаж»", "leftTopNoBorder", f1, 3, 0.0F));
        table.addCell(createCell("Строительство", "leftTopNoBorder", f1, 1, 0.0F));
        table.addCell(createCell("14.295.24 ТЕКУЩИЙ РЕМОНТ ЗДАНИЙ И СООРУЖЕНИЙ ПК «ШЕСХАРИС»", "leftCenterNoBorder", f1, 2, 0.0F));
//      4 строка
        table.addCell(createCell("Субподрядчик:", "leftTopNoBorder", f1, 3, 0.0F));
        Paragraph paragraph54 = new Paragraph("Объект: ", f1);
        PdfPCell fourthCellOnRow5 = new PdfPCell(new Phrase(paragraph54));
        cellStyler.createCellStyleHorizontalLeftAndVerticalTopNoBorder(fourthCellOnRow5);
        table.addCell(createCell("Объект: ", "leftTopNoBorder", f1, 1, 0.0F));
        table.addCell(createCell("«Текущий ремонт зданий ПП «Грушовая» ПК «Шесхарис». " +
                "Текущий ремонт». «Текущий ремонт зданий ПП «Шесхарис». " +
                "ПК «Шесхарис». Текущий ремонт", "leftTopNoBorder", f1, 2, 0.0F));
//      Заголовок
        table.addCell(createCell("РЕЕСТР", "centerBottomNoBorder", f1, 6, 50F));
        table.addCell(createCell("исполнительной  документации", "centerTopNoBorder", f1, 6, 30F));
    }

    private void addTableHeader(PdfPTable table) {
        table.addCell(createCell("№ п/п", "centerBorder", f2, 1, 0.0F));
        table.addCell(createCell("Наименование документа", "centerBorder", f2, 1, 0.0F));
        table.addCell(createCell("№ документа, дата", "centerBorder", f2, 1, 0.0F));
        table.addCell(createCell("Организация, составившая документ", "centerBorder", f2, 1, 0.0F));
        table.addCell(createCell("Кол-во листов", "centerBorder", f2, 1, 0.0F));
        table.addCell(createCell("Страница по списку", "centerBorder", f2, 1, 0.0F));
    }

    private void addPdfFooter(PdfPTable table) {
        table.addCell(createCell("Сдал", "leftBottomNoBorder", f1, 2, 50F));
        addSignature(table);
        table.addCell(createCell("Принял", "leftBottomNoBorder", f1, 2, 40F));
        addSignature(table);
    }

    private void addSignature(PdfPTable table) {
        table.addCell(createCell("", "emptyCellBottomBorder", f1, 4, 50F));
        table.addCell(createCell("", "centerNoBorder", f1, 2, 0.0F));
        table.addCell(createCell("(фамилия, инициалы)", "centerTopNoBorder", f3, 2, 0.0F));
        table.addCell(createCell("(подпись)", "centerTopNoBorder", f3, 1, 0.0F));
        table.addCell(createCell("(дата)", "centerTopNoBorder", f3, 1, 0.0F));
    }

    private PdfPCell createCell(String text, String position, Font font, int numberOfColumns, float cellHeight) {
        Paragraph paragraph = new Paragraph(text, font);
        PdfPCell cell = new PdfPCell(paragraph);

        if (numberOfColumns > 1) {
            cell.setColspan(numberOfColumns);
        }

        if(cellHeight > 0.0F) {
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
        }

        return cell;
    }

    public int numberOfPages() throws IOException {
        PdfReader reader = new PdfReader("C:\\Users\\PC\\Desktop\\work\\pdfFile.pdf");
        int pages = reader.getNumberOfPages();
        reader.close();
        return pages;
    }
}

