package com.notwork.notwork_backend.service.impl;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.notwork.notwork_backend.service.IEmbeddingService;
import com.notwork.notwork_backend.service.IMilvusService;
import io.milvus.v2.client.MilvusClientV2;
import io.milvus.v2.service.collection.request.LoadCollectionReq;
import io.milvus.v2.service.vector.request.DeleteReq;
import io.milvus.v2.service.vector.request.InsertReq;
import io.milvus.v2.service.vector.request.SearchReq;
import io.milvus.v2.service.vector.request.data.FloatVec;
import io.milvus.v2.service.vector.response.SearchResp;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.document.Document;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class MilvusServiceImpl implements IMilvusService {

    @Value("${spring.ai.vectorstore.milvus.collection-name}")
    private String collectionName;

    private final MilvusClientV2 milvusClient;
    private final IEmbeddingService embeddingService;
    private final TokenTextSplitter textSplitter;

    @Override
    public void insert(List<JsonObject> data) {
        InsertReq insertReq = InsertReq.builder()
                .collectionName(collectionName)
                .data(data)
                .build();
        milvusClient.insert(insertReq);
    }

    @Override
    public void deleteByBlogId(Long blogId) {
        String filterExpr = "blogId == " + blogId;
        DeleteReq deleteReq = DeleteReq.builder()
                .collectionName(collectionName)
                .filter(filterExpr)
                .build();
        milvusClient.delete(deleteReq);
        log.info("已删除博客 {} 在 Milvus 中的向量数据", blogId);
    }

    @Override
    public void reinsert(Long blogId, String blogTitle, String content) {
        deleteByBlogId(blogId);

        Document document = new Document(UUID.randomUUID().toString(), content, Map.of("blogId", blogId, "blogTitle", blogTitle));
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
            jsonObject.addProperty("blogId", blogId);
            jsonObject.addProperty("blogTitle", blogTitle);
            jsonObject.addProperty("blogChunk", splitList.get(i));
            jsonObject.add("blogVector", gson.toJsonTree(embeddings.get(i)));
            data.add(jsonObject);
        }
        insert(data);
        log.info("已重新插入博客 {} 到 Milvus", blogId);
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
