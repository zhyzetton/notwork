package com.notwork.notwork_backend.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")  // 对所有接口生效
                .allowedOrigins("*")  // 允许的来源域名，生产环境建议指定具体域名
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")  // 允许的请求方法
                .allowedHeaders("*")  // 允许的请求头
                .exposedHeaders("*")  // 允许前端获取的响应头
                .maxAge(3600);  // 预检请求的缓存时间（秒），减少重复预检请求
    }
}
