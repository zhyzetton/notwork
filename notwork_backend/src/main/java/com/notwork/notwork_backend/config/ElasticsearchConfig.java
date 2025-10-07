package com.notwork.notwork_backend.config;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ElasticsearchConfig {

    @Value("${spring.elasticsearch.ip}")
    private String ip;
    @Value("${spring.elasticsearch.port}")
    private Integer port;
    @Value("${spring.elasticsearch.scheme}")
    private String scheme;

    // 配置 Elasticsearch 客户端
    @Bean
    public ElasticsearchClient elasticsearchClient() {
        // 创建底层 RestClient
        RestClient restClient = RestClient.builder(
                new HttpHost(ip, port, scheme)
        ).build();

        // 创建传输层（使用 Jackson 处理 JSON）
        ElasticsearchTransport transport = new RestClientTransport(
                restClient, new JacksonJsonpMapper()
        );

        // 创建高层客户端
        return new ElasticsearchClient(transport);
    }
}