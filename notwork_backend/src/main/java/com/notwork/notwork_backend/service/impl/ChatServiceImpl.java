package com.notwork.notwork_backend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.notwork.notwork_backend.entity.pojo.Blog;
import com.notwork.notwork_backend.entity.pojo.BlogCollect;
import com.notwork.notwork_backend.mapper.BlogCollectMapper;
import com.notwork.notwork_backend.mapper.BlogMapper;
import com.notwork.notwork_backend.service.ChatService;
import com.notwork.notwork_backend.utils.MilvusTool;
import io.milvus.v2.service.vector.response.SearchResp;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {

    private final ChatClient chatClient;
    private final MilvusTool milvusTool;
    private final BlogMapper blogMapper;
    private final BlogCollectMapper blogCollectMapper;


    @Override
    public Flux<String> chat(Long userId, String query) {
        return chatClient.prompt(query)
                .advisors(advisorSpec -> advisorSpec.param("conversationId", userId))
                .stream().content();
    }

    /**
     * 聊天
     * @param userId
     * @param userQuery
     * @return
     */
    @Override
    public Flux<String> chatWithRag(Long userId, String userQuery) {
        LambdaQueryWrapper<Blog> queryWrapper1 = new LambdaQueryWrapper<>();
        LambdaQueryWrapper<BlogCollect> queryWrapper2 = new LambdaQueryWrapper<>();
        queryWrapper1.eq(Blog::getUserId, userId);
        queryWrapper2.eq(BlogCollect::getUserId, userId);
        List<Long> blogIdList = new java.util.ArrayList<>(blogMapper.selectList(queryWrapper1).stream().map(Blog::getId).toList());
        List<Long> list2 = blogCollectMapper.selectList(queryWrapper2).stream().map(BlogCollect::getBlogId).toList();
        blogIdList.addAll(list2);

        List<SearchResp.SearchResult> searchResults = milvusTool.searchOnPersonal(blogIdList, userQuery).get(0);
        String context = searchResults.stream()
                .map(result -> {
                    String blogId = result.getEntity().get("blogId").toString(); // 博客ID
                    String chunkText = result.getEntity().get("blogChunk").toString(); // 分块文本
                    return String.format("【博客ID：%s】%s", blogId, chunkText);
                })
                .collect(Collectors.joining("\n\n"));

        String promptText = String.format("""
        请基于以下上下文信息回答用户的问题。
        上下文信息：
        %s
        
        用户问题：%s
        
        回答要求：
        1. 严格基于上下文内容，不要编造信息；
        2. 若基于上下文有答案，回答以“根据您的知识库（此处写blogId）：”
        2. 若上下文没有相关内容，回答以“您的知识库中没有答案，大模型回答：”。
        """, context, userQuery);

        return chatClient.prompt(promptText)
                .advisors(advisorSpec -> advisorSpec.param("conversationId", userId))
                .stream()
                .content();
    }
}
