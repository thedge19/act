package com.act.act.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ActLogResponseDto {
    private Long id;

    private String actNumber;

    private String works;

    private String endDate;
}
