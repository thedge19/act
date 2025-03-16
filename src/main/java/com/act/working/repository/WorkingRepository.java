package com.act.working.repository;

import com.act.working.model.Working;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WorkingRepository extends JpaRepository<Working, Long> {
    List<Working> findAllBySubObjectIdOrderByIdAsc(Long id);
}
