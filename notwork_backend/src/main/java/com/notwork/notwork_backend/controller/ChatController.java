package com.notwork.notwork_backend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

import org.springframework.ai.chat.client.ChatClient;

@RestController
@RequiredArgsConstructor
public class ChatController {

    private final ChatClient chatClient;

    // WebClient 自己创建或通过 Spring 注入都可以
    private final WebClient webClient = WebClient.builder()
            .baseUrl("https://dashscope.aliyuncs.com/compatible-mode/v1")
            .defaultHeader("Authorization", "Bearer sk-69389daffede4c97add9e38a0fd7c091")
            .build();

    // 保留原来的 chat 接口
    @GetMapping("/chat")
    public Flux<String> chat(@RequestParam String userId, @RequestParam String query, @RequestParam String aiType) {
        return chatClient.prompt().user(query)
                .advisors(advisorSpec -> advisorSpec.param("conversationId", userId))
                .stream()
                .content();
    }

    // 改写 /embed 接口，直接调用兼容模式 embedding
    @GetMapping("/embed")
    public Mono<Map<String, Object>> embed(@RequestParam String text) {

        Map<String, Object> payload = Map.of(
                "model", "text-embedding-v4",
                "input", List.of(text) // 兼容模式要求 input 是数组
        );

        return webClient.post()
                .uri("/embeddings")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(payload))
                .retrieve()
                .bodyToMono(new org.springframework.core.ParameterizedTypeReference<Map<String, Object>>() {});
    }
}
