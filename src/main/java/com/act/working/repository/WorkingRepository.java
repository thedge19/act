package com.act.working.repository;

import com.act.working.model.Working;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface WorkingRepository extends JpaRepository<Working, Long> {

//    @Query("select w from Working w where w.subObject.id == id and w.done > 0 ")
    List<Working> findAllBySubObjectIdOrderByIdAsc(Long id);
}
