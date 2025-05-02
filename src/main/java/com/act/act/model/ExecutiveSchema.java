package com.act.act.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

@Table(name = "executive_schemas")
@Entity
@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
@ToString
public class ExecutiveSchema {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "schema_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "act_id")
    @NonNull
    private Act act;

    @Column(name = "schema_number")
    @NotEmpty
    private String schemasActNumber;

    @Column(name="schema_path")
    private String schemaPath;
}
