package com.example.demo.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String QUEUE_NAME = "email_queue"; // Hàng đợi chuyên chứa lệnh gửi mail
    public static final String EXCHANGE_NAME = "user_exchange";
    public static final String ROUTING_KEY = "send_welcome_email";

    @Bean
    public Queue emailQueue() {
        return new Queue(QUEUE_NAME, true);
    }

    @Bean
    public DirectExchange userExchange() {
        return new DirectExchange(EXCHANGE_NAME);
    }

    @Bean
    public Binding binding(Queue emailQueue, DirectExchange userExchange) {
        return BindingBuilder.bind(emailQueue).to(userExchange).with(ROUTING_KEY);
    }
}