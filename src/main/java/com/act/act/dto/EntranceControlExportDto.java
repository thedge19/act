package com.act.act.dto;

import com.act.act.model.Act;
import com.act.project.model.Project;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Data
@Builder
public class EntranceControlExportDto {
    private Long id;

    private String date;

    private String materials;

    private String documents;
}
