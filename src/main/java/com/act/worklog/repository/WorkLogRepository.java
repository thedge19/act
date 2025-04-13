package com.act.worklog.repository;

import com.act.worklog.model.WorkLog;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface WorkLogRepository extends JpaRepository<WorkLog, Long> {
    List<WorkLog> findAllByOrderByWorkLogNumber();

    WorkLog findFirstByWorkDate(@NotNull LocalDate workDate);
}
