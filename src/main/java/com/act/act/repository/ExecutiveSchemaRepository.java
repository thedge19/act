package com.act.act.repository;

import com.act.act.model.ExecutiveSchema;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExecutiveSchemaRepository extends JpaRepository<ExecutiveSchema, Long> {
    ExecutiveSchema findBySchemasActNumber(String executiveName);
}
