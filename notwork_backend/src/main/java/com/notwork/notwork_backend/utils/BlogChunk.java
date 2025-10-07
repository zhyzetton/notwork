package com.notwork.notwork_backend.utils;

import org.springframework.ai.document.Document;
import org.springframework.ai.transformer.splitter.TextSplitter;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class BlogChunk {

    private final TextSplitter textSplitter;

    public BlogChunk() {
        this.textSplitter = new TokenTextSplitter();
    }

    public List<String> split(String blogContent) {
        Document document = new Document(blogContent);
        return textSplitter.split(document).stream()
                .map(Document::getText)
                .collect(Collectors.toList());
    }
}
