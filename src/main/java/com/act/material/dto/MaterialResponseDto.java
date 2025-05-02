package com.act.material.dto;

import com.act.material.model.Certificate;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@Builder
public class MaterialResponseDto {
    private Long id;

    private String name;

    private String units;

    private String documents;

    private String author;

    private Integer numberOfPages;

    private String standard;

    private Long certificateId;
}
