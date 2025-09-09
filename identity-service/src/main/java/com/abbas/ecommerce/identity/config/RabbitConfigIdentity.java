package com.abbas.ecommerce.identity.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfigIdentity {

    public static final String ORDER_EVENTS_EXCHANGE = "order.events";
    public static final String USER_VALIDATION_QUEUE = "identity.user.validate.q";
    public static final String USER_VALIDATION_ROUTING_KEY = "identity.user.validate";

    // 1 Exchange
    @Bean
    public TopicExchange orderEventsExchange() {
        return new TopicExchange(ORDER_EVENTS_EXCHANGE, true, false);
    }

    // 2 Queue
    @Bean
    public Queue userValidationQueue() {
        return new Queue(USER_VALIDATION_QUEUE, true);
    }

    // 3 Binding (Exchange -> Queue)
    @Bean
    public Binding bindUserValidationQueue(Queue userValidationQueue, TopicExchange orderEventsExchange) {
        return BindingBuilder.bind(userValidationQueue)
                .to(orderEventsExchange)
                .with(USER_VALIDATION_ROUTING_KEY);
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory, Jackson2JsonMessageConverter converter) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(converter);
        return template;
    }

    @Bean
    public Jackson2JsonMessageConverter producerJackson2MessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
