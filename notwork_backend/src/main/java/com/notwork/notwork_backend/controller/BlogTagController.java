package com.notwork.notwork_backend.controller;

import com.notwork.notwork_backend.entity.BlogTag;
import com.notwork.notwork_backend.service.IBlogTagService;
import com.notwork.notwork_backend.utils.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * 博客标签表 前端控制器
 * </p>
 *
 * @author zhyzetton
 * @since 2025-10-03
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/blogTag")
public class BlogTagController {

    private final IBlogTagService blogTagService;

    @GetMapping
    public Result getBlogTags() {
        List<BlogTag> blogTagList = blogTagService.list();
        return Result.success(blogTagList);
    }

}
