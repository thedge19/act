package com.act.working.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class WorkingUpdateDto {
    private Long id;

    private String name;

    private String units;

    private BigDecimal quantity;

    private BigDecimal done;
}
