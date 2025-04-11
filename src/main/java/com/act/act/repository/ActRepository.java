package com.act.act.repository;

import com.act.act.model.Act;
import com.act.subobject.model.SubObject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface ActRepository extends JpaRepository<Act, Long> {
    Long countBySubObject(SubObject subObject);

    List<Act> findAllByEndDateBetweenOrderByEndDateAscActNumberAsc(LocalDate startDate, LocalDate endDate);

    List<Act> findAllByOrderByEndDateAscActNumberAsc();

    List<Act> findAllByOrderByStartDateAscActNumberAsc();

    List<Act> findAllByOrderByActNumberAsc();

    Act findByActNumber(String actNumber);

    @Query("SELECT a FROM Act a WHERE a.inRegistry is null ORDER BY a.endDate ASC, a.actNumber ASC")
    List<Act> findAllByOrderByEndDateAsc();
}
