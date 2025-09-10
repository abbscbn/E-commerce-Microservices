package com.abbas.ecommerce.product.config;

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
public class RabbitConfigProduct {

    public static final String ORDER_EVENTS_EXCHANGE = "order.events";

    public static final String PRODUCT_VALIDATION_QUEUE = "product.validate.q";
    public static final String PRODUCT_VALIDATION_ROUTING_KEY = "product.validate";

    //QUEUE ROLLBACK

    public static final String ORDER_ROLLBACK_STOCK_QUEUE="rollback.stock.queue";

    //ROUTING KEY

    public static final String ORDER_ROLLBACK_STOCK_ROUTING_KEY="rollback.stock";

    // 1 Exchange
    @Bean
    public TopicExchange orderEventsExchange() {
        return new TopicExchange(ORDER_EVENTS_EXCHANGE, true, false);
    }

    // 2 Queue
    @Bean
    public Queue productValidationQueue() {
        return new Queue(PRODUCT_VALIDATION_QUEUE, true);
    }

    @Bean
    public Queue productRollBackQueue() {
        return new Queue(ORDER_ROLLBACK_STOCK_QUEUE, true);
    }

    // 3 Binding (Exchange -> Queue)
    @Bean
    public Binding bindProductValidationQueue(Queue productValidationQueue, TopicExchange orderEventsExchange) {
        return BindingBuilder.bind(productValidationQueue)
                .to(orderEventsExchange)
                .with(PRODUCT_VALIDATION_ROUTING_KEY);
    }

    @Bean
    public Binding bindProductRollBackQueue(Queue productRollBackQueue, TopicExchange orderEventsExchange) {
        return BindingBuilder.bind(productRollBackQueue)
                .to(orderEventsExchange)
                .with(ORDER_ROLLBACK_STOCK_ROUTING_KEY);
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
