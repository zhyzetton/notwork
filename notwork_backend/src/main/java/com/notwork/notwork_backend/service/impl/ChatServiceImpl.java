package com.notwork.notwork_backend.service.impl;

import com.notwork.notwork_backend.common.constants.CommonConstants;
import com.notwork.notwork_backend.service.IBlogCollectService;
import com.notwork.notwork_backend.service.IBlogService;
import com.notwork.notwork_backend.service.IChatService;
import com.notwork.notwork_backend.service.IMilvusService;
import io.milvus.v2.service.vector.response.SearchResp;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements IChatService {

    private final ChatClient chatClient;
    private final IMilvusService milvusService;
    private final IBlogService blogService;
    private final IBlogCollectService blogCollectService;

    @Override
    public Flux<String> chat(Long userId, String query) {
        return chatClient.prompt(query)
                .advisors(advisorSpec -> advisorSpec.param(CommonConstants.CONVERSATION_ID, userId))
                .stream().content();
    }

    @Override
    public Flux<String> chatWithRag(Long userId, String userQuery) {
        List<Long> blogIdList = new ArrayList<>(blogService.getBlogIdsByUserId(userId));
        List<Long> collectedIds = blogCollectService.getCollectedBlogIdsByUserId(userId);
        blogIdList.addAll(collectedIds);

        List<SearchResp.SearchResult> searchResults = milvusService.searchOnPersonal(blogIdList, userQuery).get(0);
        String context = searchResults.stream()
                .map(result -> {
                    String blogId = result.getEntity().get("blogId").toString();
                    String chunkText = result.getEntity().get("blogChunk").toString();
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
        2. 若基于上下文有答案，回答以"根据您的知识库（此处写blogId）："
        2. 若上下文没有相关内容，回答以"您的知识库中没有答案，大模型回答："。
        """, context, userQuery);

        return chatClient.prompt(promptText)
                .advisors(advisorSpec -> advisorSpec.param(CommonConstants.CONVERSATION_ID, userId))
                .stream()
                .content();
    }
}
