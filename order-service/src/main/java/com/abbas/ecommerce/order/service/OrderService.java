package com.abbas.ecommerce.order.service;

import com.abbas.ecommerce.common.event.OrderCreatedEvent;
import com.abbas.ecommerce.common.exception.BaseException;
import com.abbas.ecommerce.common.exception.ErrorMessage;
import com.abbas.ecommerce.common.exception.ErrorMessageType;
import com.abbas.ecommerce.order.model.FailedMessage;
import com.abbas.ecommerce.order.model.Order;
import com.abbas.ecommerce.order.model.OrderItem;
import com.abbas.ecommerce.order.repository.OrderRepository;
import jakarta.persistence.LockModeType;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class OrderService {

    private OrderRepository orderRepository;

    private OrderEventPublisher orderEventPublisher;

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

        // Event yayınla
        List<OrderCreatedEvent.OrderItemDto> eventItems = items.stream()
                .map(i -> new OrderCreatedEvent.OrderItemDto(i.getProductId(), i.getQuantity()))
                .toList();

        OrderCreatedEvent event = new OrderCreatedEvent(savedOrder.getId(), userId, eventItems);
        orderEventPublisher.publishOrderCreatedEvent(event);


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
    public void updateOrderStatus(Long orderId, String status, List<FailedMessage> failedMessages) {
        Order order = orderRepository.findByIdForUpdate(orderId)
                .orElseThrow(() -> new BaseException(new ErrorMessage(ErrorMessageType.ORDER_NOT_FOUND,orderId.toString())));
        order.setStatus(status);
        if(failedMessages!=null){
            order.setFailedMessages(failedMessages.stream().toList());
            failedMessages.forEach(failedMessage -> failedMessage.setOrder(order));
        }
        order.setFailedMessages(failedMessages);
        orderRepository.save(order);
    }

}
