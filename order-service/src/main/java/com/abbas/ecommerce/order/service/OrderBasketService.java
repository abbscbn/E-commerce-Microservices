package com.abbas.ecommerce.order.service;

import com.abbas.ecommerce.order.model.OrderBasket;
import com.abbas.ecommerce.order.model.OrderBasketItem;
import com.abbas.ecommerce.order.repository.OrderBasketRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class OrderBasketService {
    private final OrderBasketRepository basketRepository;



    public OrderBasket getBasketById(Long id) {
        return basketRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Basket not found with id: " + id));
    }


    public List<OrderBasket> getAllBaskets() {
        return basketRepository.findAll();
    }


    public OrderBasket getBasketByUserId(Long userId) {

        Optional<OrderBasket> optOrderBasket = basketRepository.findByUserId(userId);

        if(optOrderBasket.isEmpty()){
            throw new RuntimeException("UserId ye ait Order bulunamadı");
        }

        return optOrderBasket.get();

    }


    public OrderBasket saveBasket(OrderBasket basket) {
        OrderBasket orderBasket= new OrderBasket();
        orderBasket.setUserId(basket.getUserId());

        List<OrderBasketItem> orderBasketItems= new ArrayList<>();

        orderBasketItems.addAll(basket.getItems());

        orderBasketItems.forEach((orderBasketItem -> orderBasketItem.setBasket(orderBasket)));

        orderBasket.setItems(orderBasketItems);

        return basketRepository.save(orderBasket);
    }


    public OrderBasket updateBasket(Long id, OrderBasket updatedBasket) {
        OrderBasket existing = basketRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Basket not found with id: " + id));

        // basit örnek: sadece items
        existing.getItems().clear();
        existing.getItems().addAll(updatedBasket.getItems());

        return basketRepository.save(existing);
    }


    public String deleteBasket(Long id) {
        if (!basketRepository.existsById(id)) {
            throw new RuntimeException("Basket not found with id: " + id);
        }
        basketRepository.deleteById(id);

        return "Silme başarılı";
    }

}
