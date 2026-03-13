package com.notwork.notwork_backend.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.notwork.notwork_backend.entity.dto.BlogSearchDto;
import com.notwork.notwork_backend.entity.dto.BlogSubmitDto;
import com.notwork.notwork_backend.entity.pojo.Blog;
import com.notwork.notwork_backend.entity.pojo.BlogTagRelation;
import com.notwork.notwork_backend.entity.vo.BlogSearchVo;
import com.notwork.notwork_backend.mapper.BlogMapper;
import com.notwork.notwork_backend.service.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.document.Document;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class BlogServiceImpl extends ServiceImpl<BlogMapper, Blog> implements IBlogService {

    private final IBlogTagRelationService blogTagRelationService;
    private final BlogMapper blogMapper;
    private final IElasticsearchService elasticsearchService;
    private final IEmbeddingService embeddingService;
    private final IMilvusService milvusService;
    private final TokenTextSplitter textSplitter;

    @Override
    @Transactional
    public void insertBlogAndTag(BlogSubmitDto dto, Long userId) throws IOException {
        Blog blog = new Blog();
        BeanUtils.copyProperties(dto, blog);
        blog.setUserId(userId);
        save(blog);

        BlogTagRelation blogTagRelation = new BlogTagRelation();
        blogTagRelation.setBlogId(blog.getId());
        blogTagRelation.setTagId(dto.getTagId());
        blogTagRelationService.save(blogTagRelation);

        elasticsearchService.saveBlogToEs(blog, dto.getTagId());

        String content = blog.getContentMarkdown();
        Document document = new Document(UUID.randomUUID().toString(), content, Map.of("blogId", blog.getId(), "blogTitle", blog.getTitle()));
        List<Document> chunks = textSplitter.apply(List.of(document));

        List<String> splitList = chunks.stream()
                .map(Document::getText)
                .collect(Collectors.toList());

        List<float[]> embeddings = embeddingService.embedding(splitList);

        List<JsonObject> data = new ArrayList<>();
        Gson gson = new Gson();
        for (int i = 0; i < splitList.size(); i++) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("id", chunks.get(i).getId());
            jsonObject.addProperty("blogId", blog.getId());
            jsonObject.addProperty("blogTitle", blog.getTitle());
            jsonObject.addProperty("blogChunk", splitList.get(i));
            jsonObject.add("blogVector", gson.toJsonTree(embeddings.get(i)));
            data.add(jsonObject);
        }
        milvusService.insert(data);
    }

    @Override
    public IPage<BlogSearchVo> getBlogList(BlogSearchDto dto) {
        Page<Object> page = new Page<>(dto.getPageNum(), dto.getPageSize());
        return blogMapper.searchBlog(page, dto);
    }

    @Override
    @Transactional
    public void updateBlogAndTag(Long id, BlogSubmitDto dto, Long userId) throws IOException {
        Blog blog = getById(id);
        if (blog == null) {
            throw new RuntimeException("博客不存在");
        }
        if (!blog.getUserId().equals(userId)) {
            throw new RuntimeException("无权限修改此博客");
        }

        BeanUtils.copyProperties(dto, blog, "id", "userId", "createTime");
        updateById(blog);

        // BlogTagRelation 是无独立主键的关联表，不支持 updateById
        // 改为：先删除该博客的旧关联，再插入新关联
        blogTagRelationService.remove(new LambdaQueryWrapper<BlogTagRelation>()
                .eq(BlogTagRelation::getBlogId, id));
        BlogTagRelation newRelation = new BlogTagRelation();
        newRelation.setBlogId(id);
        newRelation.setTagId(dto.getTagId());
        blogTagRelationService.save(newRelation);

        elasticsearchService.updateBlogToEs(blog, dto.getTagId());

        milvusService.reinsert(blog.getId(), blog.getTitle(), blog.getContentMarkdown());
    }

    @Override
    public List<Long> getBlogIdsByUserId(Long userId) {
        return list(new LambdaQueryWrapper<Blog>().eq(Blog::getUserId, userId))
                .stream().map(Blog::getId).toList();
    }
}
