package com.abbas.ecommerce.product.messaging;

import com.abbas.ecommerce.common.event.RollbackStockEvent;
import com.abbas.ecommerce.product.services.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductRollbackListener {

    private final ProductService productService;

    @RabbitListener(queues = "rollback.stock.queue")
    public void handleRollback(RollbackStockEvent event) {
        event.getItems().forEach(item ->
                productService.increaseStock(item.getProductId(), item.getQuantity())
        );
    }
}
