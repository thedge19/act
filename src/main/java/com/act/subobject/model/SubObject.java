package com.act.subobject.model;

import com.act.project.model.Project;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.util.Objects;

@Table(name = "subobjects")
@Entity
@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
@ToString
public class SubObject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "subobject_id")
    private Long id;

    @Column(name = "subobject_name")
    @NotEmpty
    private String name;

    @Column(name = "subobject_title")
    @NotEmpty
    private String title;

    @JoinColumn(name = "project_id")
    @NonNull
    @ManyToOne
    private Project project;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SubObject subobject = (SubObject) o;
        return Objects.equals(id, subobject.id) && Objects.equals(name, subobject.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}
