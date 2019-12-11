package com.it.core.service;


import com.it.core.pojo.ad.ContentCategory;
import com.it.core.pojo.entity.PageResult;

import java.util.List;


public interface CategoryService {

    public List<ContentCategory> findAll();

    public PageResult findPage(ContentCategory category, Integer page, Integer rows);

    public void add(ContentCategory category);

    public ContentCategory findOne(Long id);

    public void update(ContentCategory category);

    public void delete(Long[] ids);


}
