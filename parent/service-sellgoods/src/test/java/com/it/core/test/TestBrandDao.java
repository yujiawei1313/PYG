package com.it.core.test;

import com.it.core.dao.good.BrandDao;
import com.it.core.pojo.good.Brand;
import com.it.core.pojo.good.BrandQuery;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:spring/applicationContext*.xml"})
public class TestBrandDao {


    @Autowired
    BrandDao brandDao;


    @Test
    public void TestBrandAll(){
        Brand brand = brandDao.selectByPrimaryKey(1l);
        System.out.println(brand);
    }

    @Test
    public void test2(){
        List<Brand> brands = brandDao.selectByExample(null);
        for (Brand brand : brands) {
            System.out.println(brand);
        }
    }

    @Test
    public void test3(){
        BrandQuery brandQuery=new BrandQuery();
        brandQuery.setFields("id,name");
        brandQuery.setOrderByClause("id desc");
        BrandQuery.Criteria criteria = brandQuery.createCriteria();
        criteria.andIdEqualTo(2l);
        List<Brand> brands = brandDao.selectByExample(brandQuery);
        for (Brand brand : brands) {
            System.out.println(brand);
        }
    }




}
