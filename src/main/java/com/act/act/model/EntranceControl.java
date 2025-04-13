package com.act.act.model;

import com.act.project.model.Project;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Table(name = "entrance_controls")
@Entity
@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
@ToString
public class EntranceControl {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "control_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "control_project_id")
    private Project project;

    @Column(name = "control_subobject_name")
    private String subObjectName;

    @Column(name = "control_number")
    private String controlNumber;

    @Column(name = "control_date")
    private LocalDate date;

    @Column(name = "control_materials")
    private String materials;

    @Column(name = "control_documents")
    private String documents;

    @Column(name = "control_document_author")
    private String author;

    @Column(name = "control_standard")
    private String standard;

    @ManyToOne
    @JoinColumn(name = "control_act_id")
    private Act act;

    @Column(name = "control_sheet_numbers")
    private Integer controlSheetNumbers;
}
