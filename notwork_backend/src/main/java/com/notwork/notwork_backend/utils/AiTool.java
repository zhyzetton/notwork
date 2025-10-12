package com.notwork.notwork_backend.utils;

import com.alibaba.cloud.ai.dashscope.embedding.DashScopeEmbeddingOptions;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.notwork.notwork_backend.entity.pojo.Blog;
import io.milvus.v2.service.vector.response.SearchResp;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.embedding.EmbeddingRequest;
import org.springframework.ai.embedding.EmbeddingResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.util.*;
import java.util.stream.Collectors;


@RequiredArgsConstructor
@Component
@Slf4j
public class AiTool {

    private final BlogChunk blogChunk;
//    private final ChatClient chatClient;
    private final MilvusTool milvusTool;
    private final XinferenceClient xinferenceClient;
//    private final EmbeddingModel embeddingModel;
//
//    public EmbeddingResponse text2embed(String text) {
//        return embeddingModel.call(new EmbeddingRequest(List.of(text), null));
//    }

    public String chatWithRag(List<Long> blogIdList, String userQuery) {
        // Mock milvusTool if not actually injected

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
        2. 若上下文没有相关内容，按照你自己的知识回答。
        """, context, userQuery);

        return null;
    }

    public void chunkAndUpload(Blog blog) {
        List<String> splitList = blogChunk.split(blog.getContentMarkdown());
        List<List<Double>> rawEmbeddings = xinferenceClient.getEmbeddings(splitList);
        int n = splitList.size();
        List<JsonObject> data = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("id", UUID.randomUUID().toString()); // Milvus ID (VarChar)
            jsonObject.addProperty("blogId", blog.getId()); // 业务 Blog ID (Int64)
            jsonObject.addProperty("blogChunk", splitList.get(i)); // 博客内容
            JsonArray jsonVectorArray = new JsonArray();
            for (Double val : rawEmbeddings.get(i)) { // 直接使用 Double 列表
                jsonVectorArray.add(val.floatValue()); // 添加 Float 值到 JsonArray
            }
            jsonObject.add("blogVector", jsonVectorArray); // 添加 JsonArray 到 JsonObject

            data.add(jsonObject);
        }

        milvusTool.insert(data);
    }

}
