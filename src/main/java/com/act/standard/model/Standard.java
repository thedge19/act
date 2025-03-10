package com.act.standard.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

@Table(name = "standards")
@Entity
@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
@ToString
public class Standard {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "standard_id")
    private Long id;

    @NotEmpty
    @Column(name = "standard_name")
    private String name;
}