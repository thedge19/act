package com.act.registry.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RegistryUpdateRequestDto {
    private Long id;

    private Integer monthId;

    private Long rowNumber;

    private Integer listInOrder;
}
