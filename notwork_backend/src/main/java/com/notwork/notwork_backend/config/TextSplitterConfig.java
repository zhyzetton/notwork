package com.notwork.notwork_backend.config;

import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TextSplitterConfig {

    @Bean
    public TokenTextSplitter textSplitter() {
        return new TokenTextSplitter(
                1200,
                400,
                15,
                5000,
                true
        );
    }
}
