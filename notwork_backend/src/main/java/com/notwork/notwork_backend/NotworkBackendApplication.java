package com.notwork.notwork_backend;


import org.mybatis.spring.annotation.MapperScan;
import org.springframework.ai.vectorstore.milvus.autoconfigure.MilvusVectorStoreAutoConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(exclude = MilvusVectorStoreAutoConfiguration.class)
@MapperScan("com.notwork.notwork_backend.mapper")
public class NotworkBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(NotworkBackendApplication.class, args);
    }

}
