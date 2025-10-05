package com.notwork.notwork_backend.utils;

import com.notwork.notwork_backend.entity.pojo.Blog;
import lombok.RequiredArgsConstructor;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.springframework.stereotype.Component;
import java.io.IOException;
import java.util.*;


@RequiredArgsConstructor
@Component
public class EsTool {
    private final RestHighLevelClient restHighLevelClient;

    public void saveBlogToEs(Blog blog, Long tagId) throws IOException {
        Map<String, Object> map = new HashMap<>();
        map.put("id", blog.getId());
        map.put("userId", blog.getUserId());
        map.put("title", blog.getTitle());
        map.put("contentMarkdown", blog.getContentMarkdown());
        map.put("tagId", tagId);
        map.put("createTime", blog.getCreateTime());
        map.put("updateTime", blog.getUpdateTime());
        IndexRequest request = new IndexRequest("blog_index")
                .id(blog.getId().toString())
                .source(map);
        restHighLevelClient.index(request, RequestOptions.DEFAULT);
    }

    public List<Map<String, Object>> esSearchBlogWithHighlight(String keyword, int page, int size) throws IOException {
        SearchRequest searchRequest = new SearchRequest("blog_index");
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();

        // 多字段匹配
        sourceBuilder.query(QueryBuilders.multiMatchQuery(keyword, "title", "contentMarkdown"));
        sourceBuilder.from((page - 1) * size);
        sourceBuilder.size(size);

        // 高亮设置
        HighlightBuilder highlightBuilder = new HighlightBuilder();
        highlightBuilder.field("title");
        highlightBuilder.field("contentMarkdown");
        highlightBuilder.preTags("<span style=\"background-color: #fde68a\">");
        highlightBuilder.postTags("</span>");
        highlightBuilder.fragmentSize(150); // 上下文长度
        highlightBuilder.numOfFragments(1);
        sourceBuilder.highlighter(highlightBuilder);

        searchRequest.source(sourceBuilder);

        SearchResponse response = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        List<Map<String, Object>> result = new ArrayList<>();

        for (SearchHit hit : response.getHits().getHits()) {
            Map<String, Object> source = hit.getSourceAsMap();
            Map<String, Object> blogMap = new HashMap<>(source);

            // 处理高亮
            Map<String, HighlightField> highlightFields = hit.getHighlightFields();
            highlightFields.forEach((field, highlight) -> {
                String fragment = String.join("...",
                        Arrays.stream(highlight.fragments())
                                .map(Text::toString)
                                .toArray(String[]::new)
                );
                blogMap.put(field + "_highlight", fragment);
            });

            result.add(blogMap);
        }

        return result;
    }
}
