package com.notwork.notwork_backend.mq.producer;

import com.notwork.notwork_backend.config.RabbitMQConfig;
import com.notwork.notwork_backend.mq.message.BlogEventMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BlogEventProducer {

    private final RabbitTemplate rabbitTemplate;

    public void send(BlogEventMessage message) {
        rabbitTemplate.convertAndSend(RabbitMQConfig.BLOG_EXCHANGE, "", message);
    }
}
