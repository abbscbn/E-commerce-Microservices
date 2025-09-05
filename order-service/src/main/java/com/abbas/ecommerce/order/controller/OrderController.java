package com.abbas.ecommerce.order.controller;


import com.abbas.ecommerce.common.response.RootResponse;
import com.abbas.ecommerce.order.model.Order;
import com.abbas.ecommerce.order.model.OrderItem;
import com.abbas.ecommerce.order.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import java.util.List;

@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping("/save/{userId}")
    public ResponseEntity<RootResponse<Order>> createOrder(
            @PathVariable(name = "userId") Long userId,
            @RequestBody List<OrderItem> items, WebRequest webRequest
            ) {
        return ResponseEntity.ok(RootResponse.ok(orderService.createOrder(userId, items),webRequest));
    }
    @GetMapping("/get/{userId}")
    public ResponseEntity<RootResponse<Order>> getOrderByUserId (@PathVariable(name = "userId") Long userId, WebRequest webRequest){
        return ResponseEntity.ok(RootResponse.ok(orderService.getOrderByUserId(userId),webRequest));
    }
}
