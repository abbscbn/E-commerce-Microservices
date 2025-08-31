package com.abbas.ecommerce.order.controller;


import com.abbas.ecommerce.order.model.Order;
import com.abbas.ecommerce.order.model.OrderItem;
import com.abbas.ecommerce.order.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @GetMapping("/test")
    public ResponseEntity<String> test (){
       return ResponseEntity.ok("Başarılı");
    }
    @PostMapping("/save/{userId}")
    public Order createOrder(
            @PathVariable(name = "userId") Long userId,
            @RequestBody List<OrderItem> items
    ) {
        return orderService.createOrder(userId, items);
    }
}
