package com.abbas.ecommerce.order.controller;

import com.abbas.ecommerce.common.response.RootResponse;
import com.abbas.ecommerce.order.model.OrderBasket;
import com.abbas.ecommerce.order.service.OrderBasketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import java.util.List;

@RestController
@RequestMapping("/orderbasket")
public class OrderBasketController {

    @Autowired
    private OrderBasketService orderBasketService;

    @GetMapping("/{id}")
    public ResponseEntity<RootResponse<OrderBasket>> getBasketById(@PathVariable Long id, WebRequest webRequest){
       return ResponseEntity.ok(RootResponse.ok(orderBasketService.getBasketById(id),webRequest));
    }

    @GetMapping("/getall")
    public ResponseEntity<RootResponse<List<OrderBasket>>> getAllBaskets(WebRequest webRequest){
      return ResponseEntity.ok(RootResponse.ok(orderBasketService.getAllBaskets(),webRequest));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<RootResponse<OrderBasket>> getBasketByUserId(@PathVariable Long userId, WebRequest webRequest){
      return ResponseEntity.ok(RootResponse.ok(orderBasketService.getBasketByUserId(userId),webRequest));
    }

    @PostMapping("/save")
    public ResponseEntity<RootResponse<OrderBasket>> saveBasket(@RequestBody OrderBasket basket, WebRequest webRequest){
      return ResponseEntity.ok(RootResponse.ok(orderBasketService.saveBasket(basket),webRequest));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<RootResponse<OrderBasket>> updateBasket(@PathVariable Long id, OrderBasket updatedBasket, WebRequest webRequest){
     return ResponseEntity.ok(RootResponse.ok(orderBasketService.updateBasket(id,updatedBasket),webRequest));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<RootResponse<String>> deleteBasket(@PathVariable Long id, WebRequest webRequest){
       return ResponseEntity.ok(RootResponse.ok(orderBasketService.deleteBasket(id),webRequest));
    }


}
