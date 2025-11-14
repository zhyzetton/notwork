package com.notwork.notwork_backend.utils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.stereotype.Component;
import java.util.*;

@RequiredArgsConstructor
@Component
@Slf4j
public class AiTool {

    private final EmbeddingModel embeddingModel;

    public List<float[]> embedding(List<String> textList) {
        return embeddingModel.embed(textList);
    }

}
