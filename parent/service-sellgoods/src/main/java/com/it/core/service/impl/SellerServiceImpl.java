package com.it.core.service.impl;


import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.it.core.dao.seller.SellerDao;
import com.it.core.pojo.entity.PageResult;
import com.it.core.pojo.seller.Seller;
import com.it.core.pojo.seller.SellerQuery;
import com.it.core.service.SellerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
@Transactional
public class SellerServiceImpl implements SellerService {

    @Autowired
    private SellerDao sellerDao;

    @Override
    public void add(Seller seller) {
        seller.setCreateTime(new Date());
        //审核状态注册的时候默认为0 ,未审核
        seller.setStatus("0");
        sellerDao.insertSelective(seller);
    }

    @Override
    public PageResult findPage(Seller seller, Integer page, Integer rows) {
        PageHelper.startPage(page, rows);
        SellerQuery query = new SellerQuery();
        SellerQuery.Criteria criteria = query.createCriteria();
        if (seller != null) {
            if (seller.getStatus() != null && !"".equals(seller.getStatus())) {
                criteria.andStatusEqualTo(seller.getStatus());
            }
            if (seller.getName() != null && !"".equals(seller.getName())){
                criteria.andNameLike("%"+seller.getName()+"%");
            }
            if (seller.getNickName() != null && !"".equals(seller.getNickName())) {
                criteria.andNickNameLike("%"+seller.getNickName()+"%");
            }
        }

        Page<Seller> sellerList = (Page<Seller>)sellerDao.selectByExample(query);
        return new PageResult(sellerList.getTotal(), sellerList.getResult());
    }

    @Override
    public Seller findOne(String id) {
        return sellerDao.selectByPrimaryKey(id);
    }

    @Override
    public void updateStatus(String sellerId, String status) {
        Seller seller = new Seller();
        seller.setStatus(status);
        seller.setSellerId(sellerId);
        sellerDao.updateByPrimaryKeySelective(seller);
    }
}
