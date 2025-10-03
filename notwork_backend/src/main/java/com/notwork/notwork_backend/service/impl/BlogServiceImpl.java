package com.notwork.notwork_backend.service.impl;

import com.notwork.notwork_backend.entity.dto.BlogSubmitDto;
import com.notwork.notwork_backend.entity.pojo.Blog;
import com.notwork.notwork_backend.entity.pojo.BlogTagRelation;
import com.notwork.notwork_backend.mapper.BlogMapper;
import com.notwork.notwork_backend.service.IBlogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.notwork.notwork_backend.service.IBlogTagRelationService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * <p>
 * 博客表 服务实现类
 * </p>
 *
 * @author zhyzetton
 * @since 2025-10-01
 */
@RequiredArgsConstructor
@Service
public class BlogServiceImpl extends ServiceImpl<BlogMapper, Blog> implements IBlogService {

    private final IBlogTagRelationService blogTagRelationService;

    @Override
    @Transactional
    public void insertBlog(BlogSubmitDto dto) {
        Blog blog = new Blog();
        BeanUtils.copyProperties(dto, blog);
        LocalDateTime now = LocalDateTime.now();
        blog.setCreateTime(now);
        blog.setUpdateTime(now);
        save(blog);
        BlogTagRelation blogTagRelation = new BlogTagRelation();
        blogTagRelation.setBlogId(blog.getId());
        blogTagRelation.setTagId(dto.getTagId());
        blogTagRelationService.save(blogTagRelation);
    }
}
