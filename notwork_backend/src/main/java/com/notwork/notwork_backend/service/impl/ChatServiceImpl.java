package com.notwork.notwork_backend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.notwork.notwork_backend.entity.pojo.BlogTag;
import com.notwork.notwork_backend.entity.pojo.BlogTagRelation;
import com.notwork.notwork_backend.entity.vo.BlogReferenceVo;
import com.notwork.notwork_backend.entity.vo.ChatRagResult;
import com.notwork.notwork_backend.entity.vo.HistoryMessageVo;
import com.notwork.notwork_backend.service.*;
import io.milvus.v2.service.vector.response.SearchResp;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements IChatService {

    private final ChatClient chatClient;
    private final ChatMemory chatMemory;
    private final IMilvusService milvusService;
    private final IBlogService blogService;
    private final IBlogCollectService blogCollectService;
    private final IBlogTagRelationService blogTagRelationService;
    private final IBlogTagService blogTagService;

    @Override
    public Flux<String> chat(Long userId, String query) {
        return chatClient.prompt()
                .user(query)
                .advisors(a -> a.param(ChatMemory.CONVERSATION_ID, userId.toString()))
                .stream().content();
    }

    @Override
    public ChatRagResult chatWithRag(Long userId, String userQuery) {
        List<Long> blogIdList = new ArrayList<>(blogService.getBlogIdsByUserId(userId));
        List<Long> collectedIds = blogCollectService.getCollectedBlogIdsByUserId(userId);
        blogIdList.addAll(collectedIds);

        if (blogIdList.isEmpty()) {
            Flux<String> stream = chatClient.prompt()
                    .user(userQuery)
                    .advisors(a -> a.param(ChatMemory.CONVERSATION_ID, userId.toString()))
                    .stream()
                    .content();
            return new ChatRagResult(stream, List.of());
        }

        List<SearchResp.SearchResult> searchResults = milvusService.searchOnPersonal(blogIdList, userQuery).get(0);

        // 提取去重的博客引用
        LinkedHashMap<Long, BlogReferenceVo> referenceMap = new LinkedHashMap<>();
        for (SearchResp.SearchResult result : searchResults) {
            Long blogId = Long.parseLong(result.getEntity().get("blogId").toString());
            if (!referenceMap.containsKey(blogId)) {
                String blogTitle = result.getEntity().get("blogTitle").toString();

                String tagName = "";
                BlogTagRelation relation = blogTagRelationService.getOne(
                        new LambdaQueryWrapper<BlogTagRelation>().eq(BlogTagRelation::getBlogId, blogId));
                if (relation != null) {
                    BlogTag tag = blogTagService.getById(relation.getTagId());
                    if (tag != null) {
                        tagName = tag.getTagName();
                    }
                }

                BlogReferenceVo ref = new BlogReferenceVo();
                ref.setBlogId(blogId);
                ref.setTitle(blogTitle);
                ref.setTagName(tagName);
                referenceMap.put(blogId, ref);
            }
        }

        String context = searchResults.stream()
                .map(result -> {
                    String blogTitle = result.getEntity().get("blogTitle").toString();
                    String chunkText = result.getEntity().get("blogChunk").toString();
                    return String.format("【%s】%s", blogTitle, chunkText);
                })
                .collect(Collectors.joining("\n\n"));

        Flux<String> stream = chatClient.prompt()
                .system(String.format("""
                        请基于以下上下文信息回答用户的问题。
                        上下文信息：
                        %s

                        回答要求：
                        1. 严格基于上下文内容，不要编造信息；
                        2. 若基于上下文有答案，回答以"根据您的知识库："开头。
                        3. 若上下文没有相关内容，回答以"您的知识库中没有答案，大模型回答："开头。
                        """, context))
                .user(userQuery)
                .advisors(a -> a.param(ChatMemory.CONVERSATION_ID, userId.toString()))
                .stream()
                .content();
        return new ChatRagResult(stream, new ArrayList<>(referenceMap.values()));
    }

    @Override
    public List<HistoryMessageVo> getHistory(Long userId) {
        List<Message> messages = chatMemory.get(userId.toString());
        return messages.stream()
                .map(msg -> {
                    HistoryMessageVo vo = new HistoryMessageVo();
                    vo.setRole(msg.getMessageType().name().toLowerCase());
                    vo.setContent(msg.getText());
                    return vo;
                })
                .collect(Collectors.toList());
    }
}
