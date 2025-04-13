package com.act.pdf.service;

import com.act.registry.model.Registry;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfPTable;

import java.io.IOException;
import java.util.List;

public interface PdfService {
    void exportToPdf(int monthId)  throws IOException, DocumentException;

    void addTableData(PdfPTable table, List<Registry> registries) throws DocumentException, IOException;
}
