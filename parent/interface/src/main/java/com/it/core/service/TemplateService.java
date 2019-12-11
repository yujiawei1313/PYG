package com.it.core.service;

import com.it.core.pojo.entity.PageResult;
import com.it.core.pojo.template.TypeTemplate;

import java.util.List;
import java.util.Map;

public interface TemplateService {
    PageResult findPage(TypeTemplate template, Integer page, Integer rows);

    public void add(TypeTemplate template);

    public TypeTemplate findOne(Long id);

    public void update(TypeTemplate template);

    public void delete(Long[] ids);

    List<Map> findBySpecList(Long id);
}
