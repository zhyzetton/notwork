package com.notwork.notwork_backend.service.impl;

import com.google.gson.JsonObject;
import com.notwork.notwork_backend.service.IEmbeddingService;
import com.notwork.notwork_backend.service.IMilvusService;
import io.milvus.v2.client.MilvusClientV2;
import io.milvus.v2.service.collection.request.LoadCollectionReq;
import io.milvus.v2.service.vector.request.InsertReq;
import io.milvus.v2.service.vector.request.SearchReq;
import io.milvus.v2.service.vector.request.data.FloatVec;
import io.milvus.v2.service.vector.response.SearchResp;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class MilvusServiceImpl implements IMilvusService {

    @Value("${spring.ai.vectorstore.milvus.collection-name}")
    private String collectionName;

    private final MilvusClientV2 milvusClient;
    private final IEmbeddingService embeddingService;

    @Override
    public void insert(List<JsonObject> data) {
        InsertReq insertReq = InsertReq.builder()
                .collectionName(collectionName)
                .data(data)
                .build();
        milvusClient.insert(insertReq);
    }

    @Override
    public List<List<SearchResp.SearchResult>> searchOnPersonal(List<Long> blogIdList, String query) {
        List<float[]> embeddings = embeddingService.embedding(List.of(query));
        float[] floats = embeddings.get(0);
        FloatVec userVector = new FloatVec(floats);
        String filterExpr = "blogId in [" +
                blogIdList.stream()
                        .map(Object::toString)
                        .collect(Collectors.joining(", ")) +
                "]";
        milvusClient.loadCollection(LoadCollectionReq.builder()
                .collectionName(collectionName)
                .build());

        SearchReq searchReq = SearchReq.builder()
                .collectionName(collectionName)
                .data(List.of(userVector))
                .topK(5)
                .filter(filterExpr)
                .outputFields(List.of("blogId", "blogVector", "blogChunk"))
                .build();
        SearchResp searchResp = milvusClient.search(searchReq);
        List<List<SearchResp.SearchResult>> searchResults = searchResp.getSearchResults();
        log.info("Milvus 搜索完成，共返回 {} 组结果", searchResults.size());
        return searchResults;
    }
}
