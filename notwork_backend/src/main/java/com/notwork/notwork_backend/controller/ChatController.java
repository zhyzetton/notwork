package com.notwork.notwork_backend.controller;

import com.notwork.notwork_backend.common.constants.CommonConstants;
import com.notwork.notwork_backend.common.result.Result;
import com.notwork.notwork_backend.common.utils.SecurityUtils;
import com.notwork.notwork_backend.service.IChatService;
import com.notwork.notwork_backend.service.IEmbeddingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
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

    @GetMapping(produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    @Operation(summary = "与AI聊天，aiType控制是否需要RAG")
    public Flux<String> chat(
            @RequestParam @NotBlank(message = "查询内容不能为空") String query,
            @RequestParam String aiType) {
        Long userId = SecurityUtils.getCurrentUserId();
        if (Objects.equals(aiType, CommonConstants.AI_TYPE_CHAT)) {
            return chatService.chat(userId, query);
        } else {
            return chatService.chatWithRag(userId, query);
        }
    }

    @GetMapping("/embed")
    @Operation(summary = "嵌入模型接口")
    public Result<List<float[]>> embed(@RequestParam @NotBlank(message = "文本不能为空") String text) {
        return Result.success(embeddingService.embedding(List.of(text)));
    }
}
