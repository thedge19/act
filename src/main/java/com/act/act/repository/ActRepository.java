package com.act.act.repository;

import com.act.act.model.Act;
import com.act.subobject.model.SubObject;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface ActRepository extends JpaRepository<Act, Long> {
    Long countBySubObject(SubObject subObject);

    List<Act> findAllByEndDateBetweenOrderByEndDateAscActNumberAsc(LocalDate startDate, LocalDate endDate);

    List<Act> findAllByOrderByEndDateAscActNumberAsc();
}
