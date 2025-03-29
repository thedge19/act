package com.act.act.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Builder
@Data
public class ActRequestDto {
    @NotNull
    private Long projectId;

    @NotNull
    private Long subObjectId;

    @NotNull
    private Long workId;

    @NotNull
    private BigDecimal workDone;

    @NotNull
    private String startDate;

    @NotNull
    private String endDate;

    private String controlDate;

    private List<ActMaterial> actMaterials;

    @NotBlank
    private String executiveSchema;

    private Long nextWorkId;

    @Data
    @Builder
    public static class ActMaterial {
        private Long id;
        private String name;
        private String units;
        private BigDecimal quantity;
        private String documents;
        private String standard;
    }
}