package com.act.act.object.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Table(name = "objects")
@Entity
@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
public class Object {
    @Id
    @Column(name = "object_id")
    private Long id;
    @Column(name = "object_name")
    private String name;
}
