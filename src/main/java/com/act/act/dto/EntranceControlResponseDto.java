package com.act.act.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Builder
@Data
public class EntranceControlResponseDto {
    private Long id;

    private String controlNumber;

    private LocalDate date;

    private String materials;

    private String documents;

    private String author;

    private String standard;

    private Integer controlSheetNumbers;
}