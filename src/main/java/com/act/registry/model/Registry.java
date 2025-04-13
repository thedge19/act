package com.act.registry.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Table(name = "registries")
@Entity
@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
@ToString
public class Registry {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "registry_id")
    private Long id;

    @Column(name = "registry_row_number")
    private Long rowNumber;

    @Column(name = "registry_month_id")
    private Integer monthId;

    @NotBlank
    @Column(name = "document_name")
    private String documentName;

    @NotNull
    @Column(name = "document_date")
    private LocalDate documentDate;

    @NotBlank
    @Column(name = "document_number")
    private String documentNumber;

    @NotBlank
    @Column(name = "document_author")
    private String documentAuthor;

    @NotNull
    @Column(name = "number_of_sheets")
    private Integer numberOfSheets;

    @Column(name = "list_in_order")
    private Integer listInOrder;

    @Column(name = "adding_time")
    private LocalDateTime addingTime;

    @Column(name="current_act_id")
    private Long currentActId;
}
