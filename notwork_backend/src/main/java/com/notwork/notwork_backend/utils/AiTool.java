package com.notwork.notwork_backend.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.notwork.notwork_backend.entity.pojo.Blog;
import io.milvus.v2.service.vector.response.SearchResp;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;
import org.springframework.web.client.RestClient;


@RequiredArgsConstructor
@Component
@Slf4j
public class AiTool {

    private final BlogChunk blogChunk;
//    private final ChatClient chatClient;
    private final MilvusTool milvusTool;
    private final XinferenceClient xinferenceClient;
    private final RestClient restClient;
    private final ObjectMapper objectMapper;
    @Value("${spring.ai.openai.base-url}")
    private String uri;
    @Value("${spring.ai.openai.chat.options.model}")
    private String model;

    public String chatWithRag(List<Long> blogIdList, String userQuery) {
        // Mock milvusTool if not actually injected

        List<SearchResp.SearchResult> searchResults = milvusTool.searchOnPersonal(blogIdList, userQuery).get(0);
        String context = searchResults.stream()
                .map(result -> {
                    String blogId = result.getEntity().get("blogId").toString(); // 博客ID
                    String chunkText = result.getEntity().get("blogChunk").toString(); // 分块文本
                    return String.format("【博客ID：%s】%s", blogId, chunkText);
                })
                .collect(Collectors.joining("\n\n"));

        String promptText = String.format("""
        请基于以下上下文信息回答用户的问题。
        上下文信息：
        %s
        
        用户问题：%s
        
        回答要求：
        1. 严格基于上下文内容，不要编造信息；
        2. 若上下文没有相关内容，按照你自己的知识回答。
        """, context, userQuery);

        // Prepare request body
        Map<String, Object> requestBody = new HashMap<>();
        List<Map<String, String>> messages = new ArrayList<>();

        Map<String, String> messageUser = new HashMap<>();
        messageUser.put("role", "user");
        messageUser.put("content", promptText);
        messages.add(messageUser);

        // System message first, then user message as per OpenAI API common practice (though order might not strictly matter for some models)
        // If your model prefers system message at the beginning of the list, keep it like this.
        Map<String, String> messageSystem = new HashMap<>();
        messageSystem.put("role", "system");
        messageSystem.put("content", "你是一个实验室博客助手");
        messages.add(0, messageSystem); // Add system message at the beginning

        requestBody.put("messages", messages);
        requestBody.put("model", model);

        String responseBody = restClient.post()
                .uri(uri + "/chat/completions")
                .contentType(MediaType.APPLICATION_JSON)
                .body(requestBody)
                .retrieve()
                .body(String.class);

        try {
            // Use Gson to parse the JSON string
            JsonObject jsonResponse = JsonParser.parseString(responseBody).getAsJsonObject(); // Use JsonParser for parsing

            // Safely get the 'choices' array
            JsonArray choicesArray = jsonResponse.getAsJsonArray("choices");

            if (choicesArray == null || choicesArray.isEmpty()) {
                return "未从LLM获取到有效响应内容。";
            }

            List<String> contents = new ArrayList<>();
            for (JsonElement choiceElement : choicesArray) {
                JsonObject choiceObject = choiceElement.getAsJsonObject();
                if (choiceObject.has("message")) {
                    JsonObject messageObject = choiceObject.getAsJsonObject("message");
                    if (messageObject.has("content")) {
                        contents.add(messageObject.get("content").getAsString());
                    }
                }
            }
            return String.join("\n", contents);

        } catch (Exception e) {
            // Log the exception for debugging
            System.err.println("Error parsing LLM response: " + e.getMessage());
            System.err.println("Response body: " + responseBody); // Log the problematic response body
            throw new RuntimeException("Failed to parse LLM response.", e);
        }
    }

    public void chunkAndUpload(Blog blog) {
        List<String> splitList = blogChunk.split(blog.getContentMarkdown());
        List<List<Double>> rawEmbeddings = xinferenceClient.getEmbeddings(splitList);
        int n = splitList.size();
        List<JsonObject> data = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("id", UUID.randomUUID().toString()); // Milvus ID (VarChar)
            jsonObject.addProperty("blogId", blog.getId()); // 业务 Blog ID (Int64)
            jsonObject.addProperty("blogChunk", splitList.get(i)); // 博客内容
            JsonArray jsonVectorArray = new JsonArray();
            for (Double val : rawEmbeddings.get(i)) { // 直接使用 Double 列表
                jsonVectorArray.add(val.floatValue()); // 添加 Float 值到 JsonArray
            }
            jsonObject.add("blogVector", jsonVectorArray); // 添加 JsonArray 到 JsonObject

            data.add(jsonObject);
        }

        milvusTool.insert(data);
    }

}
