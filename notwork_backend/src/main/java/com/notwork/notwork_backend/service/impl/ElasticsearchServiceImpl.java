package com.notwork.notwork_backend.service.impl;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.query_dsl.MultiMatchQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.QueryBuilders;
import co.elastic.clients.elasticsearch.core.IndexRequest;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Highlight;
import co.elastic.clients.elasticsearch.core.search.Hit;
import com.notwork.notwork_backend.common.constants.CommonConstants;
import com.notwork.notwork_backend.entity.pojo.Blog;
import com.notwork.notwork_backend.service.IElasticsearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

@RequiredArgsConstructor
@Service
public class ElasticsearchServiceImpl implements IElasticsearchService {

    private final ElasticsearchClient elasticsearchClient;

    @Override
    public void saveBlogToEs(Blog blog, Long tagId) throws IOException {
        Map<String, Object> blogData = new HashMap<>();
        blogData.put("id", blog.getId());
        blogData.put("userId", blog.getUserId());
        blogData.put("title", blog.getTitle());
        blogData.put("contentMarkdown", blog.getContentMarkdown());
        blogData.put("tagId", tagId);
        blogData.put("createTime", blog.getCreateTime() == null ? null : blog.getCreateTime().toString());
        blogData.put("updateTime", blog.getUpdateTime() == null ? null : blog.getUpdateTime().toString());

        IndexRequest<Map<String, Object>> request = IndexRequest.of(r -> r
                .index(CommonConstants.ES_BLOG_INDEX)
                .id(String.valueOf(blog.getId()))
                .document(blogData)
        );

        elasticsearchClient.index(request);
    }

    @Override
    public List<Map<String, Object>> esSearchBlogWithHighlight(String keyword, int page, int size) throws IOException {
        Highlight highlightConfig = Highlight.of(h -> h
                .fields("title", f -> f
                        .preTags("<span style=\"background-color: #fde68a\">")
                        .postTags("</span>")
                        .fragmentSize(150)
                        .numberOfFragments(1)
                )
                .fields("contentMarkdown", f -> f
                        .preTags("<span style=\"background-color: #fde68a\">")
                        .postTags("</span>")
                        .fragmentSize(150)
                        .numberOfFragments(1)
                )
        );

        MultiMatchQuery query = QueryBuilders.multiMatch()
                .query(keyword)
                .fields("title^2", "contentMarkdown")
                .build();

        SearchRequest request = SearchRequest.of(s -> s
                .index(CommonConstants.ES_BLOG_INDEX)
                .query(q -> q.multiMatch(query))
                .from((page - 1) * size)
                .size(size)
                .highlight(highlightConfig)
        );

        SearchResponse<Map> response = elasticsearchClient.search(request, Map.class);

        List<Map<String, Object>> resultList = new ArrayList<>();
        for (Hit<Map> hit : response.hits().hits()) {
            Map<String, Object> source = hit.source();
            if (source == null) continue;

            Map<String, Object> result = new HashMap<>(source);

            if (hit.highlight() != null) {
                hit.highlight().forEach((field, fragments) -> {
                    String highlightText = String.join("", fragments);
                    result.put(field + "_highlight", highlightText);
                });
            }

            resultList.add(result);
        }

        return resultList;
    }
}
