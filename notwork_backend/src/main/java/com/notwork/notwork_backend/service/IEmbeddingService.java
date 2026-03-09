package com.notwork.notwork_backend.service;

import java.util.List;

public interface IEmbeddingService {

    List<float[]> embedding(List<String> textList);
}
