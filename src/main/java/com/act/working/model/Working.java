package com.act.working.model;

import com.act.standard.model.Standard;
import com.act.subobject.model.SubObject;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.math.BigDecimal;

@Table(name = "works")
@Entity
@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
@ToString
public class Working {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "work_id")
    private Long id;

    @NotBlank
    @Column(name = "work_name")
    private String name;

    @NotBlank
    @Column(name = "work_units")
    private String units;

    @Column(name = "work_quantity")
    private BigDecimal quantity;

    @Column(name = "work_done")
    private BigDecimal done;

    @JoinColumn(name = "work_standard")
    @ManyToOne
    private Standard standard;

    @JoinColumn(name = "work_subobject_id")
    @ManyToOne
    private SubObject subObject;

    @Column(name = "final_quantity", insertable = false, updatable = false)
    private BigDecimal finalQuantity;
}
