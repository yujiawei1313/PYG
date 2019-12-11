package com.it.core.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.it.core.pojo.ad.ContentCategory;
import com.it.core.pojo.entity.PageResult;
import com.it.core.pojo.entity.Result;
import com.it.core.service.CategoryService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/contentCategory")
public class CategoryController {

    @Reference
    private CategoryService categoryService;

    @RequestMapping("/findAll")
    public List<ContentCategory> findAll(){
        List<ContentCategory> list = categoryService.findAll();
        return list;

    }



    @RequestMapping("/add")
    public Result add(@RequestBody ContentCategory category) {
        try {
            categoryService.add(category);
            return new Result(true, "保存成功!");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "保存失败!");
        }
    }


    @RequestMapping("/findOne")
    public ContentCategory findOne(Long id) {
        ContentCategory one = categoryService.findOne(id);
        return one;
    }


    @RequestMapping("/update")
    public Result update(@RequestBody  ContentCategory category) {
        try {
            categoryService.update(category);
            return new Result(true, "修改成功!");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "修改失败!");
        }
    }


    @RequestMapping("/delete")
    public Result delete(Long[] ids) {
        try {
            categoryService.delete(ids);
            return new Result(true, "删除成功!");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "删除失败!");
        }
    }

    @RequestMapping("/search")
    public PageResult search(@RequestBody  ContentCategory category, Integer page, Integer rows) {
        PageResult result = categoryService.findPage(category, page, rows);
        return result;
    }


}
