package com.act.act.repository;

import com.act.act.model.Act;
import com.act.act.model.EntranceControl;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EntranceControlRepository extends JpaRepository<EntranceControl, Long> {
    List<EntranceControl> findAllByAct(Act act);
}
