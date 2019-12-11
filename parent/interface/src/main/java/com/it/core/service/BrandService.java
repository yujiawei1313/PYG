package com.it.core.service;

import com.it.core.pojo.entity.PageResult;
import com.it.core.pojo.good.Brand;

import java.util.List;
import java.util.Map;

public interface BrandService {

    List<Brand> findAll();

    PageResult findPage(Brand brand,Integer pageNum, Integer pageSize);

    void add(Brand brand);

    Brand findOne(Long id);

    void update(Brand brand);

    void delete(Long[] ids);

    List<Map> selectOptionList();
}
