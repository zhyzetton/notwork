package com.notwork.notwork_backend.service;

import com.notwork.notwork_backend.entity.vo.ChatRagResult;
import com.notwork.notwork_backend.entity.vo.HistoryMessageVo;
import reactor.core.publisher.Flux;

import java.util.List;

public interface IChatService {

    Flux<String> chat(Long userId, String query);

    ChatRagResult chatWithRag(Long userId, String userQuery);

    List<HistoryMessageVo> getHistory(Long userId);
}
