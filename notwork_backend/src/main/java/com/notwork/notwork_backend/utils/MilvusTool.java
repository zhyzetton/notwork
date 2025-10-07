package com.notwork.notwork_backend.utils;

import com.google.gson.JsonObject;
import io.milvus.v2.client.MilvusClientV2;
import io.milvus.v2.service.collection.request.LoadCollectionReq;
import io.milvus.v2.service.vector.request.InsertReq;
import io.milvus.v2.service.vector.request.SearchReq;
import io.milvus.v2.service.vector.request.data.FloatVec;
import io.milvus.v2.service.vector.response.SearchResp;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
public class MilvusTool {

    @Value("${spring.ai.vectorstore.milvus.collection-name}")
    private String collectionName;

    private final MilvusClientV2 milvusClient;
    private final XinferenceClient xinferenceClient;

    public void insert(List<JsonObject> data) {
        InsertReq insertReq = InsertReq.builder()
                .collectionName(collectionName)
                .data(data)
                .build();
        milvusClient.insert(insertReq);
    }

    public List<List<SearchResp.SearchResult>> searchOnPersonal(List<Long> blogIdList, String query) {

        // 使用自定义的HTTP客户端获取查询的embeddings
        List<List<Double>> queryEmbeddings = xinferenceClient.getEmbeddings(List.of(query));
        if (queryEmbeddings.isEmpty() || queryEmbeddings.get(0).isEmpty()) {
            throw new RuntimeException("Failed to get embedding for the query.");
        }
        List<Double> queryEmbeddingList = queryEmbeddings.get(0);
        float[] result = new float[queryEmbeddingList.size()];
        for (int i = 0; i < queryEmbeddingList.size(); i++) {
            result[i] = queryEmbeddingList.get(i).floatValue();
        }

        FloatVec userVector = new FloatVec(result);
        StringBuilder filterExpr = new StringBuilder("blogId IN [");
        // 修正了逗号和末尾括号的问题
        String ids = blogIdList.stream()
                .map(Object::toString)
                .collect(Collectors.joining(", "));
        filterExpr.append(ids);
        filterExpr.append("]");
        milvusClient.loadCollection(LoadCollectionReq.builder()
                .collectionName(collectionName)
                .build());

        SearchReq searchReq = SearchReq.builder()
                .collectionName(collectionName)
                .data(List.of(userVector))
                .topK(5)
                .filter(filterExpr.toString())
                .outputFields(List.of("blogId", "blogVector", "blogChunk"))
                .build();
        SearchResp searchResp = milvusClient.search(searchReq);
        return searchResp.getSearchResults();
    }
}