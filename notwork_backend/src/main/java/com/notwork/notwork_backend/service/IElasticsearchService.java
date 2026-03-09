package com.notwork.notwork_backend.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.notwork.notwork_backend.entity.pojo.Blog;

public interface IElasticsearchService {

    void saveBlogToEs(Blog blog, Long tagId) throws IOException;

    List<Map<String, Object>> esSearchBlogWithHighlight(String keyword, int page, int size) throws IOException;
}
