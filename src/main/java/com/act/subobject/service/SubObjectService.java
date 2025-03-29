package com.act.subobject.service;

import com.act.subobject.model.SubObject;

import java.util.List;

public interface SubObjectService {
    SubObject get(Long id);

    List<SubObject> getAll();

    List<SubObject> getAllByProjectId(long id);

    SubObject create(SubObject subObject);

    SubObject update(long id, SubObject subObject);

    void delete(long id);

    SubObject findSubObjectOrNot(long id);
}
