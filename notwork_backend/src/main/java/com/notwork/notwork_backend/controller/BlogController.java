package com.notwork.notwork_backend.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.notwork.notwork_backend.common.constants.CommonConstants;
import com.notwork.notwork_backend.common.result.Result;
import com.notwork.notwork_backend.common.utils.SecurityUtils;
import com.notwork.notwork_backend.entity.dto.BlogSearchDto;
import com.notwork.notwork_backend.entity.dto.BlogSubmitDto;
import com.notwork.notwork_backend.entity.pojo.Blog;
import com.notwork.notwork_backend.entity.vo.BlogSearchVo;
import com.notwork.notwork_backend.service.IBlogService;
import com.notwork.notwork_backend.service.IElasticsearchService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/blogs")
@Tag(name = "博客管理接口")
public class BlogController {

    private final IBlogService blogService;
    private final IElasticsearchService elasticsearchService;

    @GetMapping
    @Operation(summary = "分页查询博客列表")
    public Result<IPage<BlogSearchVo>> getBlogs(BlogSearchDto dto) {
        IPage<BlogSearchVo> blogList = blogService.getBlogList(dto);
        return Result.success(blogList);
    }

    @GetMapping("/{id}")
    @Operation(summary = "根据id查询博客")
    public Result<Blog> getBlog(@PathVariable Long id) {
        LambdaQueryWrapper<Blog> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Blog::getId, id);
        Blog blog = blogService.getOne(wrapper);
        return Result.success(blog);
    }

    @PostMapping
    @Operation(summary = "新增博客")
    public Result<Void> insertBlog(@Valid @RequestBody BlogSubmitDto dto) throws IOException {
        Long userId = SecurityUtils.getCurrentUserId();
        blogService.insertBlogAndTag(dto, userId);
        return Result.success();
    }

    @PutMapping("/{id}")
    @Operation(summary = "更新博客")
    public Result<Void> updateBlog(@PathVariable Long id, @Valid @RequestBody BlogSubmitDto dto) throws IOException {
        Long userId = SecurityUtils.getCurrentUserId();
        blogService.updateBlogAndTag(id, dto, userId);
        return Result.success();
    }

    @GetMapping("/es")
    @Operation(summary = "通过关键字，es分页查询博客")
    public Result<List<Map<String, Object>>> esSearchBlog(
            String keyword,
            Integer page,
            Integer pageSize) throws IOException {
        if (page == null || page < 1) page = CommonConstants.DEFAULT_PAGE_NUM;
        if (pageSize == null || pageSize < 1) pageSize = CommonConstants.DEFAULT_PAGE_SIZE;
        List<Map<String, Object>> maps = elasticsearchService.esSearchBlogWithHighlight(keyword, page, pageSize);
        return Result.success(maps);
    }
}
