package com.act.worklog.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;

@Table(name = "worklogs")
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class WorkLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "work_log_id")
    private Long id;

    @NotNull
    @Column(name = "work_log_date")
    LocalDate workDate;

    @NotEmpty
    @Column(name = "work_log_name")
    private String name;

    @NotNull
    @Column(name = "work_log_number")
    private Integer workLogNumber;
}

