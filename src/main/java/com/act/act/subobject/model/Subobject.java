package com.act.act.subobject.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

@Table(name = "subobjects")
@Entity
@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
public class Subobject {
    @Id
    @Column(name = "subobject_id")
    private Long id;

    @Column(name = "subobject_name")
    @NotEmpty
    private String name;

    @Column(name = "subobject_title")
    @NotEmpty
    private String title;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Subobject subobject = (Subobject) o;
        return Objects.equals(id, subobject.id) && Objects.equals(name, subobject.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}
