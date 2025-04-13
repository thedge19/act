package com.act.subobject.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SubObjectRequestDto {
    private String name;

    private String title;

    private Long projectId;
}
