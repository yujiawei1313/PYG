package com.it.core.service;


import com.it.core.pojo.ad.Content;
import com.it.core.pojo.entity.PageResult;

import java.util.List;

public interface ContentService {

    public List<Content> findAll();

    public PageResult findPage(Content content, Integer page, Integer rows);

    public void add(Content content);

    public Content findOne(Long id);

    public void update(Content content);

    public void delete(Long[] ids);

    public List<Content> findByCategoryId(Long categoryId);

    public List<Content> findByCategoryIdFromREdis(Long categoryId);


}
