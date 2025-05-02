package com.act.material.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Table(name = "materials")
@Entity
@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
@ToString
public class Material {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "material_id")
    private Long id;

    @NotEmpty
    @Column(name = "material_name")
    private String name;

    @NotBlank
    @Column(name = "material_units")
    private String units;

    @NotBlank
    @Column(name = "material_documents")
    private String documents;

    @NotBlank
    @Column(name = "author")
    private String author;

    @NotNull
    @Column(name = "number_of_pages")
    private Integer numberOfPages;

    @Column(name = "material_standard")
    private String standard;

    @OneToOne
    @JoinColumn(name = "certificate_id")
    private Certificate certificate;
}
