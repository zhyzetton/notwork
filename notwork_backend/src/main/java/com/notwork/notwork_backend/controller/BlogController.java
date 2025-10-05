package com.notwork.notwork_backend.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.notwork.notwork_backend.entity.dto.BlogSearchDto;
import com.notwork.notwork_backend.entity.dto.BlogSubmitDto;
import com.notwork.notwork_backend.entity.pojo.Blog;
import com.notwork.notwork_backend.entity.vo.BlogSearchVo;
import com.notwork.notwork_backend.service.IBlogService;
import com.notwork.notwork_backend.utils.EsTool;
import com.notwork.notwork_backend.utils.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 博客表 前端控制器
 * </p>
 *
 * @author zhyzetton
 * @since 2025-10-01
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/blog")
public class BlogController {

    private final IBlogService blogService;
    private final EsTool esTool;

    @GetMapping
    public Result getBlogs(BlogSearchDto dto) {
        IPage<BlogSearchVo> blogList = blogService.getBlogList(dto);
        return Result.success(blogList);
    }

    @GetMapping("/{id}")
    public Result getBlog(@PathVariable Long id) {
        LambdaQueryWrapper<Blog> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Blog::getId, id);
        Blog blog = blogService.getOne(wrapper);
        return Result.success(blog);
    }

    @PostMapping
    public Result insertBlog(@RequestBody BlogSubmitDto dto) throws IOException {
        blogService.insertBlogAndTag(dto);
        return Result.success();
    }

    @GetMapping("/es")
    public Result esSearchBlog(String keyword, Integer page, Integer pageSize) throws IOException {
        List<Map<String, Object>> maps = esTool.esSearchBlogWithHighlight(keyword, page, pageSize);
        return Result.success(maps);
    }
}
