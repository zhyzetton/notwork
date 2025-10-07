package com.notwork.notwork_backend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.notwork.notwork_backend.entity.dto.BlogSearchDto;
import com.notwork.notwork_backend.entity.dto.BlogSubmitDto;
import com.notwork.notwork_backend.entity.pojo.Blog;
import com.notwork.notwork_backend.entity.pojo.BlogCollect;
import com.notwork.notwork_backend.entity.pojo.BlogTagRelation;
import com.notwork.notwork_backend.entity.vo.BlogSearchVo;
import com.notwork.notwork_backend.mapper.BlogCollectMapper;
import com.notwork.notwork_backend.mapper.BlogMapper;
import com.notwork.notwork_backend.service.IBlogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.notwork.notwork_backend.service.IBlogTagRelationService;
import com.notwork.notwork_backend.utils.AiTool;
import com.notwork.notwork_backend.utils.EsTool;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;

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
    private final EsTool esTool;
    private final AiTool aiTool;
    private final BlogCollectMapper blogCollectMapper;

    @Override
    @Transactional
    public void insertBlogAndTag(BlogSubmitDto dto) throws IOException {
        Blog blog = new Blog();
        BeanUtils.copyProperties(dto, blog);
        save(blog);
        BlogTagRelation blogTagRelation = new BlogTagRelation();
        blogTagRelation.setBlogId(blog.getId());
        blogTagRelation.setTagId(dto.getTagId());
        // 保存到数据库
        blogTagRelationService.save(blogTagRelation);
        // 上传到es
        esTool.saveBlogToEs(blog, dto.getTagId());
        // 分块上传到向量数据库
        aiTool.chunkAndUpload(blog);
    }

    @Override
    public IPage<BlogSearchVo> getBlogList(BlogSearchDto dto) {
        Page<Object> page = new Page<>(dto.getPageNum(), dto.getPageSize());
        return blogMapper.searchBlog(page, dto);
    }

    @Override
    public String chatWithRag(String userId, String query) {
        LambdaQueryWrapper<Blog> queryWrapper1 = new LambdaQueryWrapper<>();
        LambdaQueryWrapper<BlogCollect> queryWrapper2 = new LambdaQueryWrapper<>();
        queryWrapper1.eq(Blog::getUserId, userId);
        queryWrapper2.eq(BlogCollect::getUserId, userId);
        List<Long> list1 = new java.util.ArrayList<>(blogMapper.selectList(queryWrapper1).stream().map(Blog::getId).toList());
        List<Long> list2 = blogCollectMapper.selectList(queryWrapper2).stream().map(BlogCollect::getBlogId).toList();
        list1.addAll(list2);
        return aiTool.chatWithRag(list1, query);
    }


}
