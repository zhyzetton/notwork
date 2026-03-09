package com.notwork.notwork_backend.service.impl;

import com.notwork.notwork_backend.service.IEmbeddingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
@Slf4j
public class EmbeddingServiceImpl implements IEmbeddingService {

    private final EmbeddingModel embeddingModel;

    @Override
    public List<float[]> embedding(List<String> textList) {
        return embeddingModel.embed(textList);
    }
}
