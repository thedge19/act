package com.act.standard.service;

import com.act.standard.model.Standard;

public interface StandardService {
    Standard get(Long id);

    Standard create(Standard standard);

    Standard update(long id, Standard standard);

    void delete(long id);

    Standard findStandardOrNot(long id);
}
