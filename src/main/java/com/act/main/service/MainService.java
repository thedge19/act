package com.act.main.service;

import com.itextpdf.text.DocumentException;

import java.io.IOException;

public interface MainService {
    void createRegistry(int id) throws IOException, DocumentException;
}
