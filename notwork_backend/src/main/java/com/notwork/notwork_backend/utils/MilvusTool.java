package com.notwork.notwork_backend.utils;

import com.google.gson.JsonObject;
import com.notwork.notwork_backend.entity.dto.EmbeddingRes;
import io.milvus.v2.client.MilvusClientV2;
import io.milvus.v2.service.collection.request.LoadCollectionReq;
import io.milvus.v2.service.vector.request.InsertReq;
import io.milvus.v2.service.vector.request.SearchReq;
import io.milvus.v2.service.vector.request.data.FloatVec;
import io.milvus.v2.service.vector.response.SearchResp;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Component
public class MilvusTool {

    @Value("${spring.ai.vectorstore.milvus.collection-name}")
    private String collectionName;

    private final MilvusClientV2 milvusClient;
    private final AiTool aiTool;

    public void insert(List<JsonObject> data) {
        InsertReq insertReq = InsertReq.builder()
                .collectionName(collectionName)
                .data(data)
                .build();
        milvusClient.insert(insertReq);
    }

    public List<List<SearchResp.SearchResult>> searchOnPersonal(List<Long> blogIdList, String query) {
        // 1️⃣ 生成向量
        EmbeddingRes block = aiTool.embed(List.of(query)).block();
        if (block == null || block.getData().isEmpty()) {
            log.error("生成 embedding 失败，结果为空");
            return Collections.emptyList();
        }

        EmbeddingRes.EmbeddingData embeddingData = block.getData().get(0);
        List<Double> embedding = embeddingData.getEmbedding();

        // 2️⃣ 转换为 FloatVec（Milvus 要求 float 类型）
        List<Float> vector = embedding.stream()
                .map(Double::floatValue)
                .collect(Collectors.toList());
        FloatVec userVector = new FloatVec(vector);

        // 3️⃣ 构造过滤条件
        String filterExpr = "blogId in [" +
                blogIdList.stream()
                        .map(Object::toString)
                        .collect(Collectors.joining(", ")) +
                "]";

        log.info("Milvus 向量搜索过滤条件: {}", filterExpr);

        // 4️⃣ 加载 collection（确保已加载）
        milvusClient.loadCollection(LoadCollectionReq.builder()
                .collectionName(collectionName)
                .build());

        // 5️⃣ 构造搜索请求
        SearchReq searchReq = SearchReq.builder()
                .collectionName(collectionName)
                .data(List.of(userVector))
                .topK(5)
                .filter(filterExpr)
                .outputFields(List.of("blogId", "blogVector", "blogChunk"))
                .build();

        // 6️⃣ 执行搜索
        SearchResp searchResp = milvusClient.search(searchReq);

        // 7️⃣ 返回结果
        List<List<SearchResp.SearchResult>> searchResults = searchResp.getSearchResults();
        log.info("Milvus 搜索完成，共返回 {} 组结果", searchResults.size());
        return searchResults;
    }

}