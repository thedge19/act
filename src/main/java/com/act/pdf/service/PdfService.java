package com.act.pdf.service;

import com.act.act.model.SelectedPeriod;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfReader;

import java.io.IOException;
import java.util.List;

public interface PdfService {
    void exportRegistryToPdf(int monthId, String path) throws IOException, DocumentException;

    int numberOfPages(String source) throws IOException;

    void exportWorkLog3ToPdf() throws IOException, DocumentException;

    void exportWorkLog6ToPdf() throws IOException, DocumentException;

    void exportEntranceControlLogToPdf() throws IOException, DocumentException;

    void exportAOSRtoPdf(SelectedPeriod selectedPeriod) throws IOException, DocumentException;

    void mergeUsingIText(List<PdfReader> pdfReaders, String path) throws IOException, DocumentException;
}
