package com.it.core.service;

import com.it.core.pojo.entity.PageResult;
import com.it.core.pojo.entity.SpecEntity;
import com.it.core.pojo.specification.Specification;

import java.util.List;
import java.util.Map;

public interface SpecificationService {
    PageResult findPage(Specification spec, Integer page, Integer rows);

    void add(SpecEntity specEntity);

    SpecEntity findOne(Long id);

    void update(SpecEntity specEntity);

    void delete(Long[] ids);

    List<Map> selectOptionList();
}
