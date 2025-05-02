package com.act.material.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Table(name = "certificates")
@Entity
@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
@ToString
public class Certificate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "certificate_id")
    private Long id;

    @OneToOne
    @JoinColumn(name = "material_id")
    private Material material;

    @NotBlank
    @Column(name="certificate_path")
    private String path;
}
