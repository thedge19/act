package com.act.subobject.repository;

import com.act.subobject.model.SubObject;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SubObjectRepository extends JpaRepository<SubObject, Long> {
    List<SubObject> findAllByProjectIdOrderByIdAsc(Long projectId);
}
