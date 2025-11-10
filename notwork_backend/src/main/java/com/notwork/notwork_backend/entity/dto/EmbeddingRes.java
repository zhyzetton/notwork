package com.notwork.notwork_backend.entity.dto;

import lombok.Data;

import java.util.List;

@Data
public class EmbeddingRes {
    private List<EmbeddingData> data;
    private String model;
    private String object;
    private Usage usage;
    private String id;

    @Data
    public static class EmbeddingData {
        private List<Double> embedding;
        private int index;
        private String object;
    }

    @Data
    public static class Usage {
        private int prompt_tokens;
        private int total_tokens;
    }
}

