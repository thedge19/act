package com.act.act.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ActResponseDto {
    private Long id;

    private String projectName;

    private String works;

    private String actNumber;

    private String startDate;

    private String endDate;

    private String materials;

    private String submittedDocuments;

    private String inAccordWith;

    private String nextWorks;

    private String inRegistry;

    private Long executiveSchemaId;
}