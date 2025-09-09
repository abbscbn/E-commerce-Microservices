package com.abbas.ecommerce.product.messaging;

import com.abbas.ecommerce.common.event.ProductValidationEvent;
import com.abbas.ecommerce.common.event.ProductValidationSucceededEvent;
import com.abbas.ecommerce.common.event.ProductValidationFailedEvent;
import com.abbas.ecommerce.common.event.ProductValidationFailedEvent.FailedItem;
import com.abbas.ecommerce.product.repository.ProductRepository;
import com.abbas.ecommerce.product.services.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ProductValidationConsumer {

    private final ProductRepository productRepository;
    private final ProductEventPublisher publisher;
    private final ProductService productService;

    @RabbitListener(queues = "product.validate.q")
    public void consume(ProductValidationEvent event) {
        System.out.println("ORDERDAN GELEN EVENT: "+event);
        List<FailedItem> failedItems = new ArrayList<>();

        event.getItems().forEach(item -> {
            productRepository.findById(item.getProductId()).ifPresentOrElse(
                    product -> {
                        if (product.getStock() < item.getQuantity()) {
                            failedItems.add(new FailedItem(
                                    product.getId(),
                                    "STOK YETERSİZ MEVCUT MİKTAR: " + product.getStock()
                            ));
                        }
                    },
                    () -> failedItems.add(new FailedItem(item.getProductId(), "ÜRÜN BULUNAMADI "+item.getProductId().toString()))
            );
        });

        if (failedItems.isEmpty()) {
            // eğer hata mesajı yoksa stok güncelle
            event.getItems().forEach(item -> productService.setProductStockByProductId(item.getProductId(), item.getQuantity()));

            List<ProductValidationSucceededEvent.Item> items= new ArrayList<>();
            event.getItems().forEach(item -> items.add(new ProductValidationSucceededEvent.Item(item.getProductId(), item.getQuantity())));
            publisher.publishValidationSucceeded(
                    new ProductValidationSucceededEvent(event.getOrderId(), items)
            );
        } else {
            publisher.publishValidationFailed(
                    new ProductValidationFailedEvent(event.getOrderId(), failedItems)
            );
        }
    }
}