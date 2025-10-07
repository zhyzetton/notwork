package com.notwork.notwork_backend.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@RequiredArgsConstructor
@Configuration
public class RestClientConfig {

    private final RestClient.Builder clientBuilder;

    @Bean
    public RestClient restClient() {
        return clientBuilder.build();
    }

}
