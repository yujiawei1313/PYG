package com.it.core.service.impl;


import com.alibaba.dubbo.config.annotation.Service;
import com.it.core.dao.item.ItemCatDao;
import com.it.core.pojo.item.ItemCat;
import com.it.core.pojo.item.ItemCatQuery;
import com.it.core.service.ItemCatService;
import com.it.core.util.Contasts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Service
public class ItemCatServiceImpl implements ItemCatService {

    @Autowired
    private ItemCatDao catDao;

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public List<ItemCat> findByParentId(Long parentId) {
        //获取所有分类数据
        List<ItemCat> itemCatAll = catDao.selectByExample(null);
        //分类名称作为key, typeId也就是模板id作为value, 缓存到redis当中
        for (ItemCat itemCat : itemCatAll) {
            redisTemplate.boundHashOps(Contasts.CATEGORY_LIST_REDIS).put(itemCat.getName(), itemCat.getTypeId());
        }
        ItemCatQuery query = new ItemCatQuery();
        ItemCatQuery.Criteria criteria = query.createCriteria();
        criteria.andParentIdEqualTo(parentId);
        List<ItemCat> itemCats = catDao.selectByExample(query);
        return itemCats;
    }

    @Override
    public ItemCat findOne(Long id) {
        return catDao.selectByPrimaryKey(id);
    }

    @Override
    public List<ItemCat> findAll() {
        return catDao.selectByExample(null);
    }
}
