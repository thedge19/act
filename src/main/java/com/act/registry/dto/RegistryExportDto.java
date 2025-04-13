package com.act.registry.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RegistryExportDto {
    private Long id;

    private Long rowNumber;

    private Integer monthId;

    private String documentName;

    private String documentDate;

    private String documentNumber;

    private String documentAuthor;

    private Integer numberOfSheets;

    private Integer listInOrder;
}
