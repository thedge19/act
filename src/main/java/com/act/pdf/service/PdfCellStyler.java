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
        pdfPCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        pdfPCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        pdfPCell.setBorder(Rectangle.NO_BORDER);
    }

    public void createCellStyleHorizontalLeftAndVerticalCenterNoBorder(PdfPCell pdfPCell) {
        pdfPCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        pdfPCell.setHorizontalAlignment(Element.ALIGN_LEFT);
        pdfPCell.setBorder(Rectangle.NO_BORDER);
    }

    public void createCellStyleHorizontalCenterAndVerticalBottomNoBorder(PdfPCell pdfPCell) {
        pdfPCell.setVerticalAlignment(Element.ALIGN_BOTTOM);
        pdfPCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        pdfPCell.setBorder(Rectangle.NO_BORDER);
    }

    public void createCellStyleHorizontalCenterAndVerticalTopNoBorder(PdfPCell pdfPCell) {
        pdfPCell.setVerticalAlignment(Element.ALIGN_TOP);
        pdfPCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        pdfPCell.setBorder(Rectangle.NO_BORDER);
    }

    public void createCellStyleHorizontalLeftAndVerticalTopNoBorder(PdfPCell pdfPCell) {
        pdfPCell.setVerticalAlignment(Element.ALIGN_TOP);
        pdfPCell.setHorizontalAlignment(Element.ALIGN_LEFT);
        pdfPCell.setBorder(Rectangle.NO_BORDER);
    }

    public void createCellStyleHorizontalLeftAndVerticalBottomNoBorder(PdfPCell pdfPCell) {
        pdfPCell.setVerticalAlignment(Element.ALIGN_BOTTOM);
        pdfPCell.setHorizontalAlignment(Element.ALIGN_LEFT);
        pdfPCell.setBorder(Rectangle.NO_BORDER);
    }

    public void createCellStyleBottomBorder(PdfPCell pdfPCell) {
        pdfPCell.setBorder(Rectangle.BOTTOM);
    }
}
