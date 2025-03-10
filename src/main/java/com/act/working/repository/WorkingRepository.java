package com.act.working.repository;

import com.act.working.model.Working;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WorkingRepository extends JpaRepository<Working, Long> {
}
