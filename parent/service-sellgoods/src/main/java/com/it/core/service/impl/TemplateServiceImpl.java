package com.it.core.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.it.core.dao.specification.SpecificationOptionDao;
import com.it.core.dao.template.TypeTemplateDao;
import com.it.core.pojo.entity.PageResult;
import com.it.core.pojo.specification.SpecificationOption;
import com.it.core.pojo.specification.SpecificationOptionQuery;
import com.it.core.pojo.template.TypeTemplate;
import com.it.core.pojo.template.TypeTemplateQuery;
import com.it.core.service.TemplateService;
import com.it.core.util.Contasts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.List;
import java.util.Map;

@Service
public class TemplateServiceImpl implements TemplateService {

    @Autowired
    private TypeTemplateDao templateDao;

    @Autowired
    private SpecificationOptionDao optionDao;

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public PageResult findPage(TypeTemplate template, Integer page, Integer rows) {

        /**
         * redis中缓存模板所有数据
         */
        List<TypeTemplate> templateAll = templateDao.selectByExample(null);
        for (TypeTemplate typeTemplate : templateAll) {
            //模板id作为key, 品牌集合作为value缓存入redis中
            String brandIdsJsonStr = typeTemplate.getBrandIds();
            //将json转换成集合
            List<Map> brandList = JSON.parseArray(brandIdsJsonStr, Map.class);
            redisTemplate.boundHashOps(Contasts.BRAND_LIST_REDIS).put(typeTemplate.getId(), brandList);

            //模板id作为key, 规格集合作为value缓存入redis中
            List<Map> specList = findBySpecList(typeTemplate.getId());
            redisTemplate.boundHashOps(Contasts.SPEC_LIST_REDIS).put(typeTemplate.getId(), specList);

        }
        PageHelper.startPage(page, rows);
        TypeTemplateQuery query = new TypeTemplateQuery();
        TypeTemplateQuery.Criteria criteria = query.createCriteria();
        if (template != null) {
            if (template.getName() != null && !"".equals(template.getName())) {
                criteria.andNameLike("%"+template.getName()+"%");
            }
        }
        Page<TypeTemplate> templateList = (Page<TypeTemplate>)templateDao.selectByExample(query);
        return new PageResult(templateList.getTotal(), templateList.getResult());
    }


    @Override
    public void add(TypeTemplate template) {
        templateDao.insertSelective(template);
    }

    @Override
    public TypeTemplate findOne(Long id) {
        return templateDao.selectByPrimaryKey(id);
    }

    @Override
    public void update(TypeTemplate template) {
        templateDao.updateByPrimaryKeySelective(template);
    }

    @Override
    public void delete(Long[] ids) {
        if (ids != null) {
            for (Long id : ids) {
                templateDao.deleteByPrimaryKey(id);
            }
        }
    }

    @Override
    public List<Map> findBySpecList(Long id) {
        //1. 根据模板id查询模板对象
        TypeTemplate typeTemplate = templateDao.selectByPrimaryKey(id);
        //2. 从模板对象中获取规格集合数据, 获取到的是json格式字符串
        //数据格式例如: [{"id":27,"text":"网络"},{"id":32,"text":"机身内存"}]
        String specIds = typeTemplate.getSpecIds();
        //3. 将json格式字符串解析成Java中的List集合对象
        List<Map> maps = JSON.parseArray(specIds, Map.class);

        //4. 遍历集合对象
        if (maps != null) {
            for (Map map : maps) {
                //5. 遍历过程中根据规格id, 查询对应的规格选项集合数据
                Long specId = Long.parseLong(String.valueOf(map.get("id")));
                //6. 将规格选项再封装到规格数据中一起返回
                SpecificationOptionQuery query = new SpecificationOptionQuery();
                SpecificationOptionQuery.Criteria criteria = query.createCriteria();
                criteria.andSpecIdEqualTo(specId);
                //根据规格id获取规格选项集合数据
                List<SpecificationOption> optionList =  optionDao.selectByExample(query);
                //将规格选项集合数据封装到原来的map中
                map.put("options", optionList);

            }
        }


        return maps;
    }
}
