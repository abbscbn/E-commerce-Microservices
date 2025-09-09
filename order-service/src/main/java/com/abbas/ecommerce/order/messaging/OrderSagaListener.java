package com.abbas.ecommerce.order.messaging;


import com.abbas.ecommerce.common.event.UserValidationSucceededEvent;
import com.abbas.ecommerce.common.event.UserValidationFailedEvent;
import com.abbas.ecommerce.order.service.OrderService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import com.abbas.ecommerce.common.event.*;
import org.springframework.amqp.core.Message;
import com.abbas.ecommerce.order.model.Order;
import com.abbas.ecommerce.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

@Component
@RequiredArgsConstructor
public class OrderSagaListener {


    private final OrderService orderService;
    private final ObjectMapper objectMapper; // <-- buraya ekle

    @RabbitListener(queues = "order.user.validation.result.q")
    public void handleUserValidationEvents(Message message) throws JsonProcessingException {
        String json = new String(message.getBody(), StandardCharsets.UTF_8);

        if (message.getMessageProperties().getReceivedRoutingKey().equals("order.user.validation.succeeded")) {
            UserValidationSucceededEvent event = objectMapper.readValue(json, UserValidationSucceededEvent.class);
            orderService.updateUserValidation(event.getOrderId(), true, null);
        } else if (message.getMessageProperties().getReceivedRoutingKey().equals("order.user.validation.failed")) {
            UserValidationFailedEvent event = objectMapper.readValue(json, UserValidationFailedEvent.class);
            orderService.updateUserValidation(event.getOrderId(), false, event.getReason());
        }
    }

    @RabbitListener(queues = "order.product.validation.result.q")
    public void handleProductValidationEvents(Message message) throws JsonProcessingException {
        String json = new String(message.getBody(), StandardCharsets.UTF_8);

        String routingKey = message.getMessageProperties().getReceivedRoutingKey();

        if (routingKey.equals("order.product.validation.succeeded")) {
            ProductValidationSucceededEvent event = objectMapper.readValue(json, ProductValidationSucceededEvent.class);
            orderService.updateProductValidation(event.getOrderId(), true, null);
        } else if (routingKey.equals("order.product.validation.failed")) {
            ProductValidationFailedEvent event = objectMapper.readValue(json, ProductValidationFailedEvent.class);
            orderService.updateProductValidation(event.getOrderId(), false, event.getFailedItems());
        }
    }
}
