package com.notwork.notwork_backend.utils;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.query_dsl.QueryBuilders;
import co.elastic.clients.elasticsearch._types.query_dsl.MultiMatchQuery;
import co.elastic.clients.elasticsearch.core.IndexRequest;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Highlight;
import co.elastic.clients.elasticsearch.core.search.Hit;
import com.notwork.notwork_backend.entity.pojo.Blog;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.*;

/**
 * 简单的 Elasticsearch 工具类
 * - 保存 Blog 文档
 * - 按关键词搜索并高亮
 */
@RequiredArgsConstructor
@Component
public class EsTool {

    private final ElasticsearchClient elasticsearchClient;

    /**
     * 保存博客到 Elasticsearch
     */
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
                .index("blog_index") // 索引名称
                .id(String.valueOf(blog.getId())) // 文档 ID
                .document(blogData)
        );

        elasticsearchClient.index(request);
    }

    /**
     * 带高亮的博客搜索
     */
    public List<Map<String, Object>> esSearchBlogWithHighlight(String keyword, int page, int size) throws IOException {
        // 高亮配置
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

        // 多字段匹配查询
        MultiMatchQuery query = QueryBuilders.multiMatch()
                .query(keyword)
                .fields("title^2", "contentMarkdown")
                .build();

        // 搜索请求
        SearchRequest request = SearchRequest.of(s -> s
                .index("blog_index")
                .query(q -> q.multiMatch(query))
                .from((page - 1) * size)
                .size(size)
                .highlight(highlightConfig)
        );

        // 执行搜索（注意：第二个参数用 Map.class）
        SearchResponse<Map> response = elasticsearchClient.search(request, Map.class);

        // 解析结果
        List<Map<String, Object>> resultList = new ArrayList<>();
        for (Hit<Map> hit : response.hits().hits()) {
            Map<String, Object> source = hit.source();
            if (source == null) continue;

            Map<String, Object> result = new HashMap<>(source);

            // 处理高亮
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
