package com.notwork.notwork_backend.controller;

import com.notwork.notwork_backend.entity.dto.BlogSubmitDto;
import com.notwork.notwork_backend.entity.pojo.Blog;
import com.notwork.notwork_backend.service.IBlogService;
import com.notwork.notwork_backend.utils.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @PostMapping
    public Result insertBlog(@RequestBody BlogSubmitDto dto) {
        blogService.insertBlog(dto);
        return Result.success();
    }
}
