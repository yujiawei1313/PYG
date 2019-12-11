package com.it.core.service.impl;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.it.core.dao.good.BrandDao;
import com.it.core.pojo.entity.PageResult;
import com.it.core.pojo.good.Brand;
import com.it.core.pojo.good.BrandQuery;
import com.it.core.service.BrandService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

@Service
public class BrandServiceImpl implements BrandService {



    @Autowired
    BrandDao brandDao;

    @Override
    public List<Brand> findAll() {
        return brandDao.selectByExample(null);
    }

    @Override
    public PageResult findPage(Brand brand, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        BrandQuery example = new BrandQuery();
        BrandQuery.Criteria criteria = example.createCriteria();
        if (brand != null) {
            if (brand.getName() != null && brand.getName().length() > 0) {
                criteria.andNameLike("%" + brand.getName() + "%");
            }
            if (brand.getFirstChar() != null && brand.getFirstChar().length() > 0) {
                criteria.andFirstCharEqualTo(brand.getFirstChar());
            }
        }
        Page<Brand> page = (Page<Brand>) brandDao.selectByExample(example);
        return new PageResult(page.getTotal(), page.getResult());

    }

    @Override
    public void add(Brand brand) {
        brandDao.insertSelective(brand);
    }

    @Override
    public Brand findOne(Long id) {
        return brandDao.selectByPrimaryKey(id);
    }

    @Override
    public void update(Brand brand) {
        brandDao.updateByPrimaryKey(brand);
    }

    @Override
    public void delete(Long[] ids) {
        if (ids != null) {
            for (Long id : ids) {
                brandDao.deleteByPrimaryKey(id);
            }
        }
    }
    @Override
    public List<Map> selectOptionList(){
    return brandDao.selectOptionList();
    }


}
