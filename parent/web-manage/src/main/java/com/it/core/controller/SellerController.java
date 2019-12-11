package com.it.core.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.it.core.pojo.entity.PageResult;
import com.it.core.pojo.entity.Result;
import com.it.core.pojo.seller.Seller;
import com.it.core.service.SellerService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/seller")
public class SellerController {

    @Reference
    private SellerService sellerService;

    @RequestMapping("/search")
    public PageResult search(@RequestBody Seller seller, Integer page, Integer rows) {
        PageResult result = sellerService.findPage(seller, page, rows);
        return result;
    }

    @RequestMapping("/findOne")
    public Seller findOne(String id) {
        return sellerService.findOne(id);
    }

    /**
     * 改变商家审核状态
     * @param sellerId  卖家id
     * @param status    状态码
     * @return
     */
    @RequestMapping("/updateStatus")
    public Result updateStatus(String sellerId, String status) {
        try {
            sellerService.updateStatus(sellerId, status);
            return new Result(true, "状态修改成功!");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "状态修改失败!");
        }
    }
}
