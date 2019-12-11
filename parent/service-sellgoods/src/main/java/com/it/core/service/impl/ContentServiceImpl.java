package com.it.core.service.impl;


import com.alibaba.druid.Constants;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.it.core.dao.ad.ContentDao;
import com.it.core.pojo.ad.Content;
import com.it.core.pojo.ad.ContentQuery;
import com.it.core.pojo.entity.PageResult;
import com.it.core.service.ContentService;
import com.it.core.util.Contasts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.List;

@Service
public class ContentServiceImpl implements ContentService {

    @Autowired
    private ContentDao contentDao;

    @Autowired
    RedisTemplate redisTemplate;

    @Override
    public List<Content> findAll() {
        return contentDao.selectByExample(null);
    }

    @Override
    public PageResult findPage(Content content, Integer page, Integer rows) {
        PageHelper.startPage(page, rows);
        ContentQuery query = new ContentQuery();
        ContentQuery.Criteria criteria = query.createCriteria();
        if (content != null) {
            if (content.getTitle() != null && !"".equals(content.getTitle())) {
                criteria.andTitleLike("%" + content.getTitle() + "%");
            }
        }
        Page<Content> contentList = (Page<Content>) contentDao.selectByExample(query);
        return new PageResult(contentList.getTotal(), contentList.getResult());
    }

    @Override
    public void add(Content content) {
        //1. 将新广告添加到数据库中
        contentDao.insertSelective(content);
        //2. 根据分类id, 到redis中删除对应分类的广告集合数据
        redisTemplate.boundHashOps(Contasts.CONTENT_LIST_REDIS).delete(content.getCategoryId());
    }

    @Override
    public Content findOne(Long id) {
        return contentDao.selectByPrimaryKey(id);
    }

    @Override
    public void update(Content content) {
        //1. 根据广告id, 到数据库中查询原来的广告对象
        Content oldContent = contentDao.selectByPrimaryKey(content.getId());
        //2. 根据原来的广告对象中的分类id, 到redis中删除对应的广告集合数据
        redisTemplate.boundHashOps(Contasts.CONTENT_LIST_REDIS).delete(oldContent.getCategoryId());
        //3. 根据传入的最新的广告对象中的分类id, 删除redis中对应的广告集合数据
        redisTemplate.boundHashOps(Contasts.CONTENT_LIST_REDIS).delete(content.getCategoryId());
        //4. 将新的广告对象更新到数据库中
        contentDao.updateByPrimaryKeySelective(content);
    }

    @Override
    public void delete(Long[] ids) {
        if (ids != null) {
            for (Long id : ids) {
                //1. 根据广告id, 到数据库中查询广告对象
                Content content = contentDao.selectByPrimaryKey(id);
                //2. 根据广告对象中的分类id, 删除redis中对应的广告集合数据
                redisTemplate.boundHashOps(Contasts.CONTENT_LIST_REDIS).delete(content.getCategoryId());
                //3. 根据广告id删除数据库中的广告数据
                contentDao.deleteByPrimaryKey(id);
            }
        }
    }

    @Override
    public List<Content> findByCategoryId(Long categoryId) {
        ContentQuery query = new ContentQuery();
        ContentQuery.Criteria criteria = query.createCriteria();
        criteria.andCategoryIdEqualTo(categoryId);
        List<Content> list = contentDao.selectByExample(query);
        return list;
    }

    @Override
    public List<Content> findByCategoryIdFromREdis(Long categoryId) {
        //首先根据分类ID到redis中取数
        List<Content> contentList = (List<Content>) redisTemplate.boundHashOps(Contasts.CONTENT_LIST_REDIS).get(categoryId);
        //如果没有再到数据中总经取一份
        if (contentList == null) {
            //取到数据在放到redis中
            contentList = findByCategoryId(categoryId);
            redisTemplate.boundHashOps(Contasts.CONTENT_LIST_REDIS).put(categoryId, contentList);
        }
        return contentList;
    }
}
