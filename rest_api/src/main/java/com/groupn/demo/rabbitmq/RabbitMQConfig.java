package com.groupn.demo.rabbitmq;
import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String QUEUE = "OCR_QUEUE";
    public static final String EXCHANGE = "DOCUMENT_EXCHANGE";
    public static final String ROUTING_KEY = "DOCUMENT_ROUTING_KEY";

    @Bean
    public Queue queue() {
        return new Queue(QUEUE, true); // durable queue
    }

    @Bean
    public TopicExchange exchange() {
        return new TopicExchange(EXCHANGE);
    }

    @Bean
    public Binding binding(Queue queue, TopicExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(ROUTING_KEY);
    }
}