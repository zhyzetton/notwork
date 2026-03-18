package com.notwork.notwork_backend.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {
    public static final String BLOG_EXCHANGE = "blog.exchange";
    public static final String ES_QUEUE = "blog.es.queue";
    public static final String VECTOR_QUEUE = "blog.vector.queue";

    @Bean
    public FanoutExchange blogExchange() {
        return new FanoutExchange(BLOG_EXCHANGE);
    }

    @Bean
    public Queue esQueue() {
        return QueueBuilder.durable(ES_QUEUE).build();
    }

    @Bean
    public Queue vectorQueue() {
        return QueueBuilder.durable(VECTOR_QUEUE).build();
    }

    @Bean
    public Binding esBinding(Queue esQueue, FanoutExchange blogExchange) {
        return BindingBuilder.bind(esQueue).to(blogExchange);
    }

    @Bean
    public Binding vectorBinding(Queue vectorQueue, FanoutExchange blogExchange) {
        return BindingBuilder.bind(vectorQueue).to(blogExchange);
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
