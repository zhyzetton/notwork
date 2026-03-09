package com.notwork.notwork_backend.controller;

import com.notwork.notwork_backend.common.result.Result;
import com.notwork.notwork_backend.entity.pojo.BlogTag;
import com.notwork.notwork_backend.service.IBlogTagService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/blogTag")
@Tag(name = "博客标签接口")
public class BlogTagController {

    private final IBlogTagService blogTagService;

    @GetMapping
    @Operation(summary = "查询所有博客标签")
    public Result<List<BlogTag>> getBlogTags() {
        List<BlogTag> blogTagList = blogTagService.list();
        return Result.success(blogTagList);
    }
}
