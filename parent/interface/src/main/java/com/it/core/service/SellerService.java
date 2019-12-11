package com.it.core.service;

import com.it.core.pojo.entity.PageResult;
import com.it.core.pojo.seller.Seller;
import org.springframework.web.bind.annotation.RequestBody;

public interface SellerService {

    public void add(Seller seller);

    public PageResult findPage(Seller seller, Integer page, Integer rows);

    public Seller findOne(String id);

    public void updateStatus(String sellerId, String status);
}
