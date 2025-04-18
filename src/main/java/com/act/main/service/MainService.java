package com.act.main.service;

import com.act.act.dto.*;
import com.act.act.model.Act;
import com.act.act.model.EntranceControl;
import com.itextpdf.text.DocumentException;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

public interface MainService {
    void createRegistry(int id) throws IOException, DocumentException;

}
