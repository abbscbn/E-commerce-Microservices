package com.abbas.ecommerce.order.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "failed_messages")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FailedMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long productId;

    private String message;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    @JsonBackReference
    private Order order;
}
