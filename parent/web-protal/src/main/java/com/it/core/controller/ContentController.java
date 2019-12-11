package com.it.core.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.it.core.pojo.ad.Content;
import com.it.core.service.ContentService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/content")
public class ContentController {

    @Reference
    private ContentService contentService;

    @RequestMapping("/findByCategoryId")
    public List<Content> findByCategoryId(Long categoryId) {
        List<Content> list = contentService.findByCategoryIdFromREdis(categoryId);
        return list;
    }
}
