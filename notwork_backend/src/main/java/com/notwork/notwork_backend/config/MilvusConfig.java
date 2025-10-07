package com.notwork.notwork_backend.config;

import io.milvus.v2.client.ConnectConfig;
import io.milvus.v2.client.MilvusClientV2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MilvusConfig {

    @Value("${spring.ai.vectorstore.milvus.client.uri}")
    private String uri;

    @Value("${spring.ai.vectorstore.milvus.client.token}")
    private String token;

    @Bean
    public MilvusClientV2 getMilvusClientV2() {
        ConnectConfig connectConfig = ConnectConfig.builder()
                .uri(uri)
                .token(token).build();
        return new MilvusClientV2(connectConfig);
    }

}
