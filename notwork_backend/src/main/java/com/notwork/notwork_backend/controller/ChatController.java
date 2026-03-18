package com.notwork.notwork_backend.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.notwork.notwork_backend.common.constants.CommonConstants;
import com.notwork.notwork_backend.common.result.Result;
import com.notwork.notwork_backend.common.utils.SecurityUtils;
import com.notwork.notwork_backend.entity.vo.ChatRagResult;
import com.notwork.notwork_backend.entity.vo.HistoryMessageVo;
import com.notwork.notwork_backend.service.IChatService;
import com.notwork.notwork_backend.service.IEmbeddingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

import org.springframework.http.MediaType;

import java.util.List;
import java.util.Objects;

@RestController
@RequiredArgsConstructor
@RequestMapping("/chat")
@Validated
@Tag(name = "AI聊天接口")
public class ChatController {

    private final IChatService chatService;
    private final IEmbeddingService embeddingService;
    private final ObjectMapper objectMapper;

    @GetMapping(produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    @Operation(summary = "与AI聊天，aiType控制是否需要RAG")
    public Flux<ServerSentEvent<String>> chat(
            @RequestParam String query,
            @RequestParam String aiType) {

        Long userId = SecurityUtils.getCurrentUserId();

        if (Objects.equals(aiType, CommonConstants.AI_TYPE_CHAT)) {
            return chatService.chat(userId, query)
                    .map(msg -> ServerSentEvent.builder(msg).build());
        }

        ChatRagResult result = chatService.chatWithRag(userId, query);

        String referencesJson;
        try {
            referencesJson = objectMapper.writeValueAsString(result.references());
        } catch (JsonProcessingException e) {
            referencesJson = "[]";
        }

        String finalJson = referencesJson;
        return result.stream()
                .map(msg -> ServerSentEvent.<String>builder().data(msg).build())
                .concatWith(Flux.just(
                        ServerSentEvent.<String>builder()
                                .event("references")
                                .data(finalJson)
                                .build()
                ));
    }

    @GetMapping("/embed")
    @Operation(summary = "嵌入模型接口")
    public Result<List<float[]>> embed(@RequestParam @NotBlank(message = "文本不能为空") String text) {
        return Result.success(embeddingService.embedding(List.of(text)));
    }

    @GetMapping("/history")
    @Operation(summary = "获取聊天历史记录")
    public Result<List<HistoryMessageVo>> getHistory() {
        Long userId = SecurityUtils.getCurrentUserId();
        return Result.success(chatService.getHistory(userId));
    }
}
