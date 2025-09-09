package com.abbas.ecommerce.order.messaging;


import com.abbas.ecommerce.common.event.UserValidationSucceededEvent;
import com.abbas.ecommerce.common.event.UserValidationFailedEvent;
import com.abbas.ecommerce.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import com.abbas.ecommerce.common.event.*;

@Component
@RequiredArgsConstructor
public class OrderSagaListener {


    private final OrderService orderService;


    @RabbitListener(queues = "order.user.validation.succeeded.q")
    public void handleUserValidationSucceeded(UserValidationSucceededEvent event) {
        System.out.println("IDENTITY → SUCCESS: " + event);
        orderService.updateUserValidation(event.getOrderId(), true, null);
    }

    @RabbitListener(queues = "order.user.validation.failed.q")
    public void handleUserValidationFailed(UserValidationFailedEvent event) {
        System.out.println("IDENTITY → FAILED: " + event);
        orderService.updateUserValidation(event.getOrderId(), false, event.getReason());
    }

    @RabbitListener(queues = "order.product.validation.succeeded.q")
    public void handleProductValidationSucceeded(ProductValidationSucceededEvent event) {
        System.out.println("PRODUCT → SUCCESS: " + event);
        orderService.updateProductValidation(event.getOrderId(), true, null);
    }

    @RabbitListener(queues = "order.product.validation.failed.q")
    public void handleProductValidationFailed(ProductValidationFailedEvent event) {
        System.out.println("PRODUCT → FAILED: " + event);
        orderService.updateProductValidation(event.getOrderId(), false, event.getFailedItems());
    }
}
