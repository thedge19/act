package com.act.act.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class EntranceControlRequestDto {
    private Long id;

    private String author;

    private Integer controlSheetNumbers;
}