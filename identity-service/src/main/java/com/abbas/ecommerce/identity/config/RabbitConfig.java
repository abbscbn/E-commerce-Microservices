package com.abbas.ecommerce.identity.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;


@Configuration
public class RabbitConfig {

    public static final String ORDER_EXCHANGE = "order-exchange";

    //orderdan gelen evente göre identity den hata mesajı

    public static final String ORDER_FAILED_QUEUE_IDENTITY = "order-failed-queue-identity";
    public static final String ORDER_FAILED_ROUTING_KEY_IDENTITY = "order.failed.identity";

    // Sipariş oluşturma
    public static final String ORDER_QUEUE = "order-queue";
    public static final String ORDER_ROUTING_KEY = "order.created";

    // Sipariş başarısız olursa
    public static final String ORDER_FAILED_QUEUE = "order-failed-queue";
    public static final String ORDER_FAILED_ROUTING_KEY = "order.failed";

    public static final String ORDER_COMPLETED_QUEUE = "order-completed-queue";
    public static final String ORDER_COMPLETED_ROUTING_KEY = "order.completed";

    // Başarısız olursa identity
    @Bean
    public Queue orderFailedQueueIdentity() {
        return new Queue(ORDER_FAILED_QUEUE_IDENTITY, true);
    }

    @Bean
    public Binding orderFailedBindingIdentity(Queue orderFailedQueueIdentity, DirectExchange orderExchange) {
        return BindingBuilder.bind(orderFailedQueueIdentity).to(orderExchange).with(ORDER_FAILED_ROUTING_KEY_IDENTITY);
    }

    @Bean
    public Queue orderCompletedQueue() {
        return new Queue(ORDER_COMPLETED_QUEUE, true);
    }

    @Bean
    public Binding orderCompletedBinding(Queue orderCompletedQueue, DirectExchange orderExchange) {
        return BindingBuilder.bind(orderCompletedQueue).to(orderExchange).with(ORDER_COMPLETED_ROUTING_KEY);
    }

    @Bean
    public DirectExchange orderExchange() {
        return new DirectExchange(ORDER_EXCHANGE);
    }

    // Başarılı sipariş kuyruğu
    @Bean
    public Queue orderQueue() {
        return new Queue(ORDER_QUEUE, true);
    }

    @Bean
    public Binding orderBinding(Queue orderQueue, DirectExchange orderExchange) {
        return BindingBuilder.bind(orderQueue).to(orderExchange).with(ORDER_ROUTING_KEY);
    }

    // Başarısız sipariş kuyruğu
    @Bean
    public Queue orderFailedQueue() {
        return new Queue(ORDER_FAILED_QUEUE, true);
    }

    @Bean
    public Binding orderFailedBinding(Queue orderFailedQueue, DirectExchange orderExchange) {
        return BindingBuilder.bind(orderFailedQueue).to(orderExchange).with(ORDER_FAILED_ROUTING_KEY);
    }

    @Bean
    public Jackson2JsonMessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory, Jackson2JsonMessageConverter messageConverter) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(messageConverter);
        return template;
    }
}
