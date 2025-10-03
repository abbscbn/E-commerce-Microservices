package com.abbas.ecommerce.order.service;

import com.abbas.ecommerce.common.exception.BaseException;
import com.abbas.ecommerce.common.exception.ErrorMessage;
import com.abbas.ecommerce.common.exception.ErrorMessageType;
import com.abbas.ecommerce.order.model.OrderBasket;
import com.abbas.ecommerce.order.model.OrderBasketItem;
import com.abbas.ecommerce.order.repository.OrderBasketRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
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
            throw new BaseException(new ErrorMessage(ErrorMessageType.ORDER_BASKET_NOT_FOUND_BY_USERID,""));
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


    public OrderBasket updateBasket(OrderBasket updatedBasket) {
        OrderBasket existing = basketRepository.findByUserId(updatedBasket.getUserId())
                .orElseThrow(() -> new BaseException(new ErrorMessage(ErrorMessageType.ORDER_BASKET_NOT_FOUND_BY_USERID,updatedBasket.getUserId().toString())));

        // eski item’ları sil
        existing.getItems().clear();

        // yeni item’ları ekle
        for (OrderBasketItem item : updatedBasket.getItems()) {
            item.setBasket(existing);
            existing.getItems().add(item);
        }

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
