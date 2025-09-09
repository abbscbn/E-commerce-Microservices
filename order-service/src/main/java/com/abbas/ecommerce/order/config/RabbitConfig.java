package com.abbas.ecommerce.order.config;

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
public class RabbitConfig {

    public static final String ORDER_EVENTS_EXCHANGE = "order.events";

    // Routing keys
    public static final String RK_USER_VALIDATE = "identity.user.validate";

    public static final String RK_PRODUCT_VALIDATE = "product.validate";

    public static final String RK_USER_VALIDATION_SUCCEEDED = "order.user.validation.succeeded";
    public static final String RK_USER_VALIDATION_FAILED = "order.user.validation.failed";

    public static final String RK_PRODUCT_VALIDATION_SUCCEEDED = "order.product.validation.succeeded";
    public static final String RK_PRODUCT_VALIDATION_FAILED = "order.product.validation.failed";

    // Queues //

    //USER VALİD Queues
    public static final String USER_VALIDATION_SUCCEEDED_RESULT_QUEUE="order.user.validation.succeeded.q";
    public static final String USER_VALIDATION_FAILED_RESULT_QUEUE="order.user.validation.failed.q";
    //PRODUCT VALİD Queues
    public static final String PRODUCT_VALIDATION_SUCCEEDED_RESULT_QUEUE = "order.product.validation.succeeded.q";
    public static final String PRODUCT_VALIDATION_FAILED_RESULT_QUEUE = "order.product.validation.failed.q";




    // 1 Exchange
    @Bean
    public TopicExchange orderEventsExchange() {
        return new TopicExchange(ORDER_EVENTS_EXCHANGE, true, false);
    }

    // 2 Queues
    @Bean
    public Queue userValidationResultSuccessQueue() {
        return new Queue(USER_VALIDATION_SUCCEEDED_RESULT_QUEUE, true);
    }
    @Bean
    public Queue userValidationResultFailedQueue() {
        return new Queue(USER_VALIDATION_FAILED_RESULT_QUEUE, true);
    }

    @Bean
    public Queue productValidationResulSuccesstQueue() {
        return new Queue(PRODUCT_VALIDATION_SUCCEEDED_RESULT_QUEUE, true);
    }
    @Bean
    public Queue productValidationResulFailedtQueue() {
        return new Queue(PRODUCT_VALIDATION_FAILED_RESULT_QUEUE, true);
    }

    // 3 Bindings
    @Bean
    public Binding bindUserValidationSucceededQueue(Queue userValidationResultSuccessQueue, TopicExchange orderEventsExchange) {
        return BindingBuilder.bind(userValidationResultSuccessQueue)
                .to(orderEventsExchange)
                .with(RK_USER_VALIDATION_SUCCEEDED);
    }

    @Bean
    public Binding bindUserValidationFailedQueue(Queue userValidationResultFailedQueue, TopicExchange orderEventsExchange) {
        return BindingBuilder.bind(userValidationResultFailedQueue)
                .to(orderEventsExchange)
                .with(RK_USER_VALIDATION_FAILED);
    }

    @Bean
    public Binding bindProductValidationSucceededQueue(Queue productValidationResulSuccesstQueue, TopicExchange orderEventsExchange) {
        return BindingBuilder.bind(productValidationResulSuccesstQueue)
                .to(orderEventsExchange)
                .with(RK_PRODUCT_VALIDATION_SUCCEEDED);
    }

    @Bean
    public Binding bindProductValidationFailedQueue(Queue productValidationResulFailedtQueue, TopicExchange orderEventsExchange) {
        return BindingBuilder.bind(productValidationResulFailedtQueue)
                .to(orderEventsExchange)
                .with(RK_PRODUCT_VALIDATION_FAILED);
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
