package com.abbas.ecommerce.order.service;

import com.abbas.ecommerce.common.event.ProductValidationFailedEvent.FailedItem;
import com.abbas.ecommerce.order.domain.OrderRollbackDomainEvent;
import org.springframework.context.ApplicationEventPublisher;
import com.abbas.ecommerce.order.domain.OrderCreatedDomainEvent;
import com.abbas.ecommerce.common.exception.BaseException;
import com.abbas.ecommerce.common.exception.ErrorMessage;
import com.abbas.ecommerce.common.exception.ErrorMessageType;
import com.abbas.ecommerce.order.model.Order;
import com.abbas.ecommerce.order.model.OrderItem;
import com.abbas.ecommerce.order.repository.OrderRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;

    private final ApplicationEventPublisher eventPublisher;

    @Transactional
    public Order createOrder(Long userId, List<OrderItem> items){

        double totelPrice=items.stream().
                mapToDouble(item-> item.getPrice()* item.getQuantity()).sum();

        Order order= Order.builder()
                .userId(userId)
                .totalPrice(totelPrice)
                .createDate(LocalDateTime.now())
                .items(items)
                .status("PENDING")
                .build();

        items.forEach(item-> item.setOrder(order));

        Order savedOrder = orderRepository.save(order);

        // -----> AFTER_COMMIT’te publish edilecek domain event:
        List<OrderCreatedDomainEvent.Item> eventItems = items.stream()
                .map(it -> new OrderCreatedDomainEvent.Item(it.getProductId(), it.getQuantity()))
                .collect(Collectors.toList());

        eventPublisher.publishEvent(
                new OrderCreatedDomainEvent(savedOrder.getId(), savedOrder.getUserId(), eventItems)
        );

        return savedOrder;

    }


    public List<Order> getOrderByUserId(Long userId){

        List<Order> orderList= new ArrayList<>();
        List<Optional<Order>> optOrder = orderRepository.findByUserId(userId);


        if(optOrder.isEmpty()){
           throw new BaseException(new ErrorMessage(ErrorMessageType.ORDER_NOT_FOUND,userId.toString()));
        }
        optOrder.forEach(order -> orderList.add(order.get()));

        return orderList;

    }

    public List<Order> getAllOrders(){
        List<Order> allOrders = orderRepository.findAll();
        if(allOrders.isEmpty()){
            throw new BaseException(new ErrorMessage(ErrorMessageType.ORDER_NOT_FOUND,"Kayıtlı hiç bir order bulunmamaktadır"));

        }

        return allOrders;
    }

    @Transactional
    public void updateUserValidation(Long orderId, boolean success, String reason) {
        Order order = orderRepository.findByIdForUpdate(orderId).orElseThrow();

        if (!success) {
            order.setStatus("FAILED");
            order.addFailedMessage(order.getUserId(), null, "User validation failed: " + reason);
        } else {
            order.markUserValidated();
        }

        finalizeOrderIfReady(order);
        orderRepository.save(order);
    }

    @Transactional
    public void updateProductValidation(Long orderId, boolean success, List<FailedItem> failedItems) {
        Order order = orderRepository.findByIdForUpdate(orderId).orElseThrow();

        if (!success) {
            order.setStatus("FAILED");
            failedItems.forEach(item ->
                    order.addFailedMessage(null, item.getProductId(), item.getReason())
            );
        } else {
            order.markProductValidated();
        }

        finalizeOrderIfReady(order);
        orderRepository.save(order);
    }

    private void finalizeOrderIfReady(Order order) {
        if (order.isUserValidated() && order.isProductValidated() && "PENDING".equals(order.getStatus())) {
            order.setStatus("CONFIRMED");
        }

        // rollback sadece: product OK && user FAIL
        if ("FAILED".equals(order.getStatus())
                && order.isProductValidated()
                && !order.isUserValidated()) {

            eventPublisher.publishEvent(new OrderRollbackDomainEvent(order.getId()));
        }
    }
    }



