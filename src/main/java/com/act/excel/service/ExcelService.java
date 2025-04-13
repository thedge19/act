package com.act.excel.service;

import com.act.act.model.SelectedPeriod;

import java.io.IOException;

public interface ExcelService {
    void writeExcel(SelectedPeriod selectedPeriod) throws IOException;

    void removeSheets() throws IOException;

    void writeExcelControl() throws IOException;
}
