package com.act.registry.repository;

import com.act.registry.model.Registry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RegistryRepository extends JpaRepository<Registry, Long> {
    Long countByMonthId(int monthId);

    @Query("SELECT r FROM Registry r WHERE r.monthId = :monthId ORDER BY r.addingTime ASC")
    List<Registry> findAllByOrderByAddingTimeAsc(int monthId);

    List<Registry> findAllByCurrentActId(Long id);

    @Query("SELECT r FROM Registry r WHERE   r.monthId = :monthId AND r.documentName = :name")
    Registry findByMonthId(Integer monthId, String name);

//    @Query("SELECT r FROM Registry r WHERE r.monthId = :monthId " +
//            "AND r.documentName = 'Общий журнал работ'")
//    Registry findByDocumentName(Integer monthId, String name);
}
