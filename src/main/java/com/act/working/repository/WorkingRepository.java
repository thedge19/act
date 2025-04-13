package com.act.working.repository;

import com.act.working.model.Working;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface WorkingRepository extends JpaRepository<Working, Long> {

    List<Working> findAllBySubObjectIdOrderByIdAsc(Long id);

    @Query("SELECT w FROM Working w WHERE w.subObject.id = :id AND w.finalQuantity > 0 ORDER BY w.name ASC")
    List<Working> findAllBySubObjectId(Long id);
}
