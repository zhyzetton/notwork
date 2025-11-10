package com.notwork.notwork_backend.controller;

import com.notwork.notwork_backend.service.ChatService;
import com.notwork.notwork_backend.service.IBlogService;
import com.notwork.notwork_backend.utils.AiTool;
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
public class ChatController {

    private final ChatClient chatClient;
    private final IBlogService blogService;
    private final AiTool aiTool;
    private final ChatService chatService;

    // 保留原来的 chat 接口
    @GetMapping("/chat")
    public Flux<String> chat(@RequestParam Long userId, @RequestParam String query, @RequestParam String aiType) {
        if (Objects.equals(aiType, "chat")) {
            return chatService.chat(userId, query);
        } else {
            return chatService.chatWithRag(userId, query);
        }
    }

    @GetMapping("/embed")
    public List<float[]> embed(String text) {
        return aiTool.embedding(List.of(text));
    }


}
