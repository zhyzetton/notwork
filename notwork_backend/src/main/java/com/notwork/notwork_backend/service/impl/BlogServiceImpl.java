package com.notwork.notwork_backend.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.notwork.notwork_backend.entity.dto.BlogSearchDto;
import com.notwork.notwork_backend.entity.dto.BlogSubmitDto;
import com.notwork.notwork_backend.entity.pojo.Blog;
import com.notwork.notwork_backend.entity.pojo.BlogTagRelation;
import com.notwork.notwork_backend.entity.vo.BlogSearchVo;
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
    private final BlogMapper blogMapper;


    @Override
    @Transactional
    public void insertBlogAndTag(BlogSubmitDto dto) {
        Blog blog = new Blog();
        BeanUtils.copyProperties(dto, blog);
        save(blog);
        BlogTagRelation blogTagRelation = new BlogTagRelation();
        blogTagRelation.setBlogId(blog.getId());
        blogTagRelation.setTagId(dto.getTagId());
        blogTagRelationService.save(blogTagRelation);
    }

    @Override
    public IPage<BlogSearchVo> getBlogList(BlogSearchDto dto) {
        Page<Object> page = new Page<>(dto.getPageNum(), dto.getPageSize());
        return blogMapper.searchBlog(page, dto);
    }
}
