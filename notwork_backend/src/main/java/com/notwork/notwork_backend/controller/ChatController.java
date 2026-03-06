package com.notwork.notwork_backend.controller;

import com.notwork.notwork_backend.service.IChatService;
import com.notwork.notwork_backend.service.IBlogService;
import com.notwork.notwork_backend.utils.AiTool;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.Objects;
import org.springframework.ai.chat.client.ChatClient;

@RestController
@RequiredArgsConstructor
@Tag(name = "chat接口")
public class ChatController {

    private final ChatClient chatClient;
    private final IBlogService blogService;
    private final AiTool aiTool;
    private final IChatService chatService;

    @GetMapping("/chat")
    @Operation(summary = "与ai聊天，aiType控制是否需要rag")
    public Flux<String> chat(@RequestParam Long userId, @RequestParam String query, @RequestParam String aiType) {
        if (Objects.equals(aiType, "chat")) {
            return chatService.chat(userId, query);
        } else {
            return chatService.chatWithRag(userId, query);
        }
    }

    @GetMapping("/embed")
    @Operation(summary = "嵌入模型接口")
    public List<float[]> embed(String text) {
        return aiTool.embedding(List.of(text));
    }


}
