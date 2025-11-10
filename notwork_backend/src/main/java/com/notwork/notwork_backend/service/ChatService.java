package com.notwork.notwork_backend.service;

import reactor.core.publisher.Flux;

import java.util.List;

public interface ChatService {

    Flux<String> chat(Long userId, String query);
    Flux<String> chatWithRag(Long userId, String userQuery);
}
