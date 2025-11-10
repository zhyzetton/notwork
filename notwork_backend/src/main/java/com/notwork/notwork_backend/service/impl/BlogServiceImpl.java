package com.notwork.notwork_backend.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.notwork.notwork_backend.entity.dto.BlogSearchDto;
import com.notwork.notwork_backend.entity.dto.BlogSubmitDto;
import com.notwork.notwork_backend.entity.dto.EmbeddingRes;
import com.notwork.notwork_backend.entity.pojo.Blog;
import com.notwork.notwork_backend.entity.pojo.BlogTagRelation;
import com.notwork.notwork_backend.entity.vo.BlogSearchVo;
import com.notwork.notwork_backend.mapper.BlogMapper;
import com.notwork.notwork_backend.service.IBlogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.notwork.notwork_backend.service.IBlogTagRelationService;
import com.notwork.notwork_backend.utils.AiTool;
import com.notwork.notwork_backend.utils.EsTool;
import com.notwork.notwork_backend.utils.MilvusTool;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.document.Document;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

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
    private final MilvusTool milvusTool;
    private final TokenTextSplitter textSplitter;

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
        // 步骤1: 构造 Document 对象
        String content = blog.getContentMarkdown();
        Document document = new Document(UUID.randomUUID().toString(), content, Map.of("blogId", blog.getId()));

        // 步骤2: 使用 TokenTextSplitter 分割
        TokenTextSplitter splitter = new TokenTextSplitter(
                1200,  // 块大小 1200 token，适合 RAG 和 Tongyi 模型
                400,   // 最小 400 字符，确保语义完整
                15,    // 最小嵌入字符数，过滤过短块
                5000,  // 最大 5000 块
                true   // 保留分隔符
        );
        List<Document> chunks = splitter.apply(List.of(document));

        // 步骤3: 转换为 List<String> 以兼容原有逻辑
        List<String> splitList = chunks.stream()
                .map(Document::getText)
                .collect(Collectors.toList());

        // 步骤4: 生成嵌入
        EmbeddingRes block = aiTool.embed(splitList).block();
        List<EmbeddingRes.EmbeddingData> embeddingData = block.getData();

        // 步骤5: 构造 JsonObject 并存储到 Milvus
        List<JsonObject> data = new ArrayList<>();
        Gson gson = new Gson();
        for (int i = 0; i < splitList.size(); i++) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("id", chunks.get(i).getId()); // 使用 Document 的 ID
            jsonObject.addProperty("blogId", blog.getId());
            jsonObject.addProperty("blogChunk", splitList.get(i));
            jsonObject.add("blogVector", gson.toJsonTree(embeddingData.get(i).getEmbedding()));
            data.add(jsonObject);
        }
        milvusTool.insert(data);
    }

    @Override
    public IPage<BlogSearchVo> getBlogList(BlogSearchDto dto) {
        Page<Object> page = new Page<>(dto.getPageNum(), dto.getPageSize());
        return blogMapper.searchBlog(page, dto);
    }


}
