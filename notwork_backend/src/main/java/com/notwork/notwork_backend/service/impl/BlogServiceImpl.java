package com.notwork.notwork_backend.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.notwork.notwork_backend.entity.dto.BlogSearchDto;
import com.notwork.notwork_backend.entity.dto.BlogSubmitDto;
import com.notwork.notwork_backend.entity.pojo.Blog;
import com.notwork.notwork_backend.entity.pojo.BlogTagRelation;
import com.notwork.notwork_backend.entity.vo.BlogSearchVo;
import com.notwork.notwork_backend.mapper.BlogMapper;
import com.notwork.notwork_backend.mq.message.BlogEventMessage;
import com.notwork.notwork_backend.mq.producer.BlogEventProducer;
import com.notwork.notwork_backend.service.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;

import java.util.*;

@RequiredArgsConstructor
@Service
public class BlogServiceImpl extends ServiceImpl<BlogMapper, Blog> implements IBlogService {

    private final IBlogTagRelationService blogTagRelationService;
    private final BlogMapper blogMapper;
    private final BlogEventProducer blogEventProducer;

    @Override
    @Transactional
    public void insertBlogAndTag(BlogSubmitDto dto, Long userId) {
        Blog blog = new Blog();
        BeanUtils.copyProperties(dto, blog);
        blog.setUserId(userId);
        save(blog);

        BlogTagRelation blogTagRelation = new BlogTagRelation();
        blogTagRelation.setBlogId(blog.getId());
        blogTagRelation.setTagId(dto.getTagId());
        blogTagRelationService.save(blogTagRelation);

        BlogEventMessage message = new BlogEventMessage();
        message.setType("CREATE");
        message.setBlogId(blog.getId());
        message.setTagId(dto.getTagId());

        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            public void afterCommit() {
                blogEventProducer.send(message);
            }
        });
    }

    @Override
    public IPage<BlogSearchVo> getBlogList(BlogSearchDto dto) {
        Page<Object> page = new Page<>(dto.getPageNum(), dto.getPageSize());
        return blogMapper.searchBlog(page, dto);
    }

    @Override
    @Transactional
    public void updateBlogAndTag(Long id, BlogSubmitDto dto, Long userId) {
        Blog blog = getById(id);
        if (blog == null) {
            throw new RuntimeException("博客不存在");
        }
        if (!blog.getUserId().equals(userId)) {
            throw new RuntimeException("无权限修改此博客");
        }

        BeanUtils.copyProperties(dto, blog, "id", "userId", "createTime");
        updateById(blog);

        blogTagRelationService.remove(new LambdaQueryWrapper<BlogTagRelation>()
                .eq(BlogTagRelation::getBlogId, id));
        BlogTagRelation newRelation = new BlogTagRelation();
        newRelation.setBlogId(id);
        newRelation.setTagId(dto.getTagId());
        blogTagRelationService.save(newRelation);

        BlogEventMessage message = new BlogEventMessage();
        message.setType("UPDATE");
        message.setBlogId(id);
        message.setTagId(dto.getTagId());

        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            public void afterCommit() {
                blogEventProducer.send(message);
            }
        });
    }

    @Override
    public List<Long> getBlogIdsByUserId(Long userId) {
        return list(new LambdaQueryWrapper<Blog>().eq(Blog::getUserId, userId))
                .stream().map(Blog::getId).toList();
    }
}
