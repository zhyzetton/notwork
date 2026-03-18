package com.notwork.notwork_backend.mq.consumer;

import com.notwork.notwork_backend.config.RabbitMQConfig;
import com.notwork.notwork_backend.entity.pojo.Blog;
import com.notwork.notwork_backend.mq.message.BlogEventMessage;
import com.notwork.notwork_backend.service.IBlogService;
import com.notwork.notwork_backend.service.IElasticsearchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class BlogEsConsumer {

    private final IBlogService blogService;
    private final IElasticsearchService elasticsearchService;

    @RabbitListener(queues = RabbitMQConfig.ES_QUEUE)
    public void handle(BlogEventMessage msg) {
        try {
            Blog blog = blogService.getById(msg.getBlogId());
            if (blog == null) {
                log.warn("ES消费者: 博客不存在, blogId={}", msg.getBlogId());
                return;
            }

            switch (msg.getType()) {
                case "CREATE" -> elasticsearchService.saveBlogToEs(blog, msg.getTagId());
                case "UPDATE" -> elasticsearchService.updateBlogToEs(blog, msg.getTagId());
                default -> log.warn("ES消费者: 未知消息类型 {}", msg.getType());
            }
            log.info("ES消费者: 处理完成, type={}, blogId={}", msg.getType(), msg.getBlogId());
        } catch (Exception e) {
            log.error("ES消费者: 处理失败, blogId={}", msg.getBlogId(), e);
            throw new RuntimeException(e);
        }
    }
}
