package com.act.working.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Builder
@Data
public class WorkingRequestDto {
    private Long id;

    @NotBlank
    private String name;

    @NotBlank
    private String units;

    @NotNull
    private BigDecimal quantity;

    private BigDecimal done;

    @NotNull
    private Long standardId;

    @NotNull
    private Long subObjectId;
}
