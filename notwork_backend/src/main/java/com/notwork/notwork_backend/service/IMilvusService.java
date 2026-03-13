package com.notwork.notwork_backend.service;

import com.google.gson.JsonObject;
import io.milvus.v2.service.vector.response.SearchResp;

import java.util.List;

public interface IMilvusService {

    void insert(List<JsonObject> data);

    void deleteByBlogId(Long blogId);

    void reinsert(Long blogId, String blogTitle, String content);

    List<List<SearchResp.SearchResult>> searchOnPersonal(List<Long> blogIdList, String query);
}
