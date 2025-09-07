package com.abbas.ecommerce.order.controller;


import com.abbas.ecommerce.common.response.RootResponse;
import com.abbas.ecommerce.order.model.Order;
import com.abbas.ecommerce.order.model.OrderItem;
import com.abbas.ecommerce.order.service.OrderService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import java.util.List;

@RestController
@RequestMapping("/order")
@Validated
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping("/save/{userId}")
    public ResponseEntity<RootResponse<Order>> createOrder(@Positive(message = "userId pozitif olmak zorundadır")
            @PathVariable(name = "userId") Long userId, @Valid
            @RequestBody List<OrderItem> items, WebRequest webRequest
    ) {
        return ResponseEntity.ok(RootResponse.ok(orderService.createOrder(userId, items), webRequest));
    }

    @GetMapping("/get/{userId}")
    public ResponseEntity<RootResponse<List<Order>>> getOrderByUserId(@PathVariable(name = "userId") Long userId, WebRequest webRequest) {
        return ResponseEntity.ok(RootResponse.ok(orderService.getOrderByUserId(userId), webRequest));
    }

    @GetMapping("/get")
    public ResponseEntity<RootResponse<List<Order>>> getAllOrders(WebRequest webRequest){
       return ResponseEntity.ok(RootResponse.ok(orderService.getAllOrders(),webRequest));
    }
}
