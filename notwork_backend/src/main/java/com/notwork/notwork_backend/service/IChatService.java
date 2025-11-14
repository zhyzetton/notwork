package com.notwork.notwork_backend.service;

import reactor.core.publisher.Flux;

public interface IChatService {

    Flux<String> chat(Long userId, String query);
    Flux<String> chatWithRag(Long userId, String userQuery);
}
