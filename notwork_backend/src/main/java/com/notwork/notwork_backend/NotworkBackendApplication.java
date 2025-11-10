package com.notwork.notwork_backend;


import org.springframework.ai.vectorstore.milvus.autoconfigure.MilvusVectorStoreAutoConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(exclude = MilvusVectorStoreAutoConfiguration.class)
public class NotworkBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(NotworkBackendApplication.class, args);
    }

}
