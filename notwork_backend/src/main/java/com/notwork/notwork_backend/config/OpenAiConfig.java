//package com.notwork.notwork_backend.config;
//
//import org.springframework.ai.chat.client.ChatClient;
//import org.springframework.ai.openai.OpenAiChatModel;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//@Configuration
//public class OpenAiConfig {
//
//    @Bean
//    public ChatClient chatClient(OpenAiChatModel model) {
//        return ChatClient.builder(model)
//                .defaultSystem("你是一个实验室博客助手")
//                .build();
//    }
//
//}
