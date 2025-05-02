package com.act.pdf.service;

import com.itextpdf.text.Element;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;

public class PdfCellStyler {
    public void createCellStyleHorizontalCenterBorder(PdfPCell pdfPCell) {
        pdfPCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        pdfPCell.setHorizontalAlignment(Element.ALIGN_CENTER);
    }

    public void createCellStyleHorizontalCenterAndVerticalCenter(PdfPCell pdfPCell) {
        pdfPCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        pdfPCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        pdfPCell.setBorder(Rectangle.NO_BORDER);
    }

    public void createCellStyleHorizontalLeftAndVerticalCenterNoBorder(PdfPCell pdfPCell) {
        pdfPCell.setHorizontalAlignment(Element.ALIGN_LEFT);
        pdfPCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        pdfPCell.setBorder(Rectangle.NO_BORDER);
    }

    public void createCellStyleHorizontalCenterAndVerticalBottomNoBorder(PdfPCell pdfPCell) {
        pdfPCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        pdfPCell.setVerticalAlignment(Element.ALIGN_BOTTOM);
        pdfPCell.setBorder(Rectangle.NO_BORDER);
    }

    public void createCellStyleHorizontalCenterAndVerticalTopNoBorder(PdfPCell pdfPCell) {
        pdfPCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        pdfPCell.setVerticalAlignment(Element.ALIGN_TOP);
        pdfPCell.setBorder(Rectangle.NO_BORDER);
    }

    public void createCellStyleHorizontalLeftAndVerticalTopNoBorder(PdfPCell pdfPCell) {
        pdfPCell.setHorizontalAlignment(Element.ALIGN_LEFT);
        pdfPCell.setVerticalAlignment(Element.ALIGN_TOP);
        pdfPCell.setBorder(Rectangle.NO_BORDER);
    }

    public void createCellStyleHorizontalLeftAndVerticalBottomNoBorder(PdfPCell pdfPCell) {
        pdfPCell.setHorizontalAlignment(Element.ALIGN_LEFT);
        pdfPCell.setVerticalAlignment(Element.ALIGN_BOTTOM);
        pdfPCell.setBorder(Rectangle.NO_BORDER);
    }

    public void createCellStyleBottomBorder(PdfPCell pdfPCell) {
        pdfPCell.setBorder(Rectangle.BOTTOM);
    }

    public void createCellStyleHorizontalRightAndVerticalBottomNoBorder(PdfPCell pdfPCell) {
        pdfPCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        pdfPCell.setVerticalAlignment(Element.ALIGN_BOTTOM);
        pdfPCell.setBorder(Rectangle.NO_BORDER);
    }

    public void createCellStyleHorizontalRightAndVerticalCenterNoBorder(PdfPCell pdfPCell) {
        pdfPCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        pdfPCell.setVerticalAlignment(Element.ALIGN_CENTER);
        pdfPCell.setBorder(Rectangle.NO_BORDER);
    }

    public void createCellStyleHorizontalRightAndVerticalTopNoBorder(PdfPCell pdfPCell) {
        pdfPCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        pdfPCell.setVerticalAlignment(Element.ALIGN_TOP);
        pdfPCell.setBorder(Rectangle.NO_BORDER);
    }

    public void createCellStyleHorizontalCenterAndVerticalCenterBottomBorder(PdfPCell pdfPCell) {
        pdfPCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        pdfPCell.setVerticalAlignment(Element.ALIGN_CENTER);
        pdfPCell.setBorder(Rectangle.BOTTOM);
    }

    public void createCellStyleHorizontalCenterAndVerticalBottomBottomBorder(PdfPCell pdfPCell) {
        pdfPCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        pdfPCell.setVerticalAlignment(Element.ALIGN_BOTTOM);
        pdfPCell.setBorder(Rectangle.BOTTOM);
    }

    public void createCellStyleHorizontalLeftAndVerticalBottomBottomBorder(PdfPCell pdfPCell) {
        pdfPCell.setHorizontalAlignment(Element.ALIGN_LEFT);
        pdfPCell.setVerticalAlignment(Element.ALIGN_BOTTOM);
        pdfPCell.setBorder(Rectangle.BOTTOM);
    }
}
