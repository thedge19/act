package com.act.act.object.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Table(name = "objects")
@Entity
public class Object {
    @Id
    @Column(name = "object_id")
    private Long id;
    @Column(name = "object_name")
    private String name;
}
