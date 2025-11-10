package com.notwork.notwork_backend.utils;

import com.notwork.notwork_backend.entity.dto.EmbeddingRes;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.*;

@RequiredArgsConstructor
@Component
@Slf4j
public class AiTool {

    @Value("${spring.ai.dashscope.api-key}")
    private String apiKey;

    @Value("${spring.ai.dashscope.base-url}")
    private String baseUrl;

    @Value("${spring.ai.dashscope.embedding.options.model}")
    private String embeddingModelName;

    private final EmbeddingModel embeddingModel;
    private final WebClient webClient = WebClient.builder()
            .baseUrl(baseUrl)
            .defaultHeader("Authorization", "Bearer " + apiKey)
            .build();

    /**
     * 嵌入，返回嵌入结果
     *
     * @param textList
     * @return
     */
    public Mono<EmbeddingRes> embed(List<String> textList) {
        Map<String, Object> payload = Map.of(
                "model", embeddingModelName,
                "input", textList
        );

        return webClient.post()
                .uri("/embeddings")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(payload))
                .retrieve()
                .bodyToMono(EmbeddingRes.class);
    }

    public List<float[]> embedding(List<String> textList) {
        return embeddingModel.embed(textList);
    }

}
