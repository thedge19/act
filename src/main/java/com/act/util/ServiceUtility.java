//package com.act.util;
//
//import com.act.pdf.service.PdfCellStyler;
//import com.itextpdf.text.Font;
//import com.itextpdf.text.FontFactory;
//import com.itextpdf.text.Paragraph;
//import com.itextpdf.text.pdf.PdfPCell;
//import com.itextpdf.text.pdf.PdfPTable;
//
//public class ServiceUtility {
//    PdfCellStyler cellStyler = new PdfCellStyler();
//
//    public static final String FONT = "./src/main/resources/fonts/timesnewromanpsmt.ttf";
//    private final Font fontToFillInControl = FontFactory.getFont(FONT, "cp1251", true, 11, Font.BOLDITALIC);
//    private final Font baseControlFont = FontFactory.getFont(FONT, "cp1251", true, 11);
//    private final Font substringControlFont = FontFactory.getFont(FONT, "cp1251", true, 8);
//
//    public static PdfPCell createCell(String text, String position, Font font, int numberOfColumns, int numberOfRows, float cellHeight) {
//        Paragraph paragraph = new Paragraph(text, font);
//        PdfPCell cell = new PdfPCell(paragraph);
//
//        if (numberOfColumns > 1) {
//            cell.setColspan(numberOfColumns);
//        }
//
//        if (numberOfRows > 1) {
//            cell.setRowspan(numberOfRows);
//        }
//
//        if (cellHeight > 0.0F) {
//            cell.setFixedHeight(cellHeight);
//        }
//
//        switch (position) {
//            case "centerBorder":
//                cellStyler.createCellStyleHorizontalCenterBorder(cell);
//                break;
//            case "centerNoBorder":
//                cellStyler.createCellStyleHorizontalCenterAndVerticalCenter(cell);
//                break;
//            case "centerBottomNoBorder":
//                cellStyler.createCellStyleHorizontalCenterAndVerticalBottomNoBorder(cell);
//                break;
//            case "leftTopNoBorder":
//                cellStyler.createCellStyleHorizontalLeftAndVerticalTopNoBorder(cell);
//                break;
//            case "leftCenterNoBorder":
//                cellStyler.createCellStyleHorizontalLeftAndVerticalCenterNoBorder(cell);
//                break;
//            case "leftBottomNoBorder":
//                cellStyler.createCellStyleHorizontalLeftAndVerticalBottomNoBorder(cell);
//                break;
//            case "emptyCellBottomBorder":
//                cellStyler.createCellStyleBottomBorder(cell);
//                break;
//            case "centerTopNoBorder":
//                cellStyler.createCellStyleHorizontalCenterAndVerticalTopNoBorder(cell);
//                break;
//            case "rightBottomNoBorder":
//                cellStyler.createCellStyleHorizontalRightAndVerticalBottomNoBorder(cell);
//                break;
//            case "rightCenterNoBorder":
//                cellStyler.createCellStyleHorizontalRightAndVerticalCenterNoBorder(cell);
//                break;
//            case "rightTopNoBorder":
//                cellStyler.createCellStyleHorizontalRightAndVerticalTopNoBorder(cell);
//                break;
//            case "centerBorderBottom":
//                cellStyler.createCellStyleHorizontalCenterAndVerticalCenterBottomBorder(cell);
//                break;
//            case "centerBottomBorderBottom":
//                cellStyler.createCellStyleHorizontalCenterAndVerticalBottomBottomBorder(cell);
//                break;
//        }
//
//        return cell;
//    }
//
//    public void addLongString(String works, PdfPTable table) {
//        while (works.length() >= 118) {
//            String worksRow = works.substring(0, 117);
//            int lastSpace = worksRow.lastIndexOf(" ");
//            worksRow = worksRow.substring(0, lastSpace);
//            table.addCell(createCell(worksRow, "centerBorderBottom", fontToFillIn, 36, 1, 0.0F));
//            works = works.replace(worksRow, "");
//        }
//        table.addCell(createCell(works, "centerBorderBottom", fontToFillIn, 36, 1, 0.0F));
//    }
//}
