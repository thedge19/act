package com.act.subobject.service;

import com.act.subobject.model.SubObject;

public interface SubObjectService {
    SubObject get(Long id);

    SubObject create(SubObject subObject);

    SubObject update(long id, SubObject subObject);

    void delete(long id);

    SubObject findSubObjectOrNot(long id);
}
