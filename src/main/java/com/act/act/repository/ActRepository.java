package com.act.act.repository;

import com.act.act.model.Act;
import com.act.subobject.model.SubObject;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ActRepository extends JpaRepository<Act, Long> {
    Long countBySubObject(SubObject subObject);
}
