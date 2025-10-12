package com.notwork.notwork_backend.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class XinferenceClient {
    private final RestClient restClient;
    private final ObjectMapper objectMapper; // 用于手动序列化/反序列化 JSON

//    @Value("${spring.ai.openai.base-url}") // 沿用你application-dev.yml中的base-url
    private String baseUrl = "http://10.0.33.13:9997/v1";

//    @Value("${spring.ai.embedding.options.model}") // 沿用你application-dev.yml中的model
    private String embeddingModelName = "bge-large-zh-v1.5";

    public XinferenceClient(RestClient.Builder restClientBuilder, ObjectMapper objectMapper) {
        this.restClient = restClientBuilder.build();
        this.objectMapper = objectMapper;
    }

    public List<List<Double>> getEmbeddings(List<String> texts) {
        if (texts == null || texts.isEmpty()) {
            return Collections.emptyList();
        }

        // 1. 构建请求体
        // Xinference通常接受一个input字段，其值可以是字符串数组或单个字符串
        // 这里我们按照OpenAI API的规范，使用List<String>
        Map<String, Object> requestBody = Map.of(
                "model", embeddingModelName,
                "input", texts
        );

        // 2. 发送POST请求
        String responseBody = restClient.post()
                .uri(baseUrl + "/embeddings") // 确保这里的URI是正确的，匹配Xinference的端点
                .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
                .body(requestBody) // RestClient会自动将Map序列化为JSON
                .retrieve()
                .body(String.class); // 先获取字符串，再手动解析

        // 3. 解析响应
        try {
            // 响应结构通常是 { "data": [ { "embedding": [...] } ], "model": "..." }
            // 你需要根据Xinference实际返回的结构来调整解析逻辑
            // 假设返回结构与OpenAI标准一致
            Map<String, Object> responseMap = objectMapper.readValue(responseBody, Map.class);
            List<Map<String, Object>> data = (List<Map<String, Object>>) responseMap.get("data");
            if (data == null) {
                throw new RuntimeException("Xinference embedding response missing 'data' field.");
            }
            return data.stream()
                    .map(item -> (List<Double>) item.get("embedding"))
                    .collect(Collectors.toList());

        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to parse Xinference embedding response", e);
        } catch (ClassCastException e) {
            throw new RuntimeException("Xinference embedding response has unexpected structure", e);
        }
    }
}
