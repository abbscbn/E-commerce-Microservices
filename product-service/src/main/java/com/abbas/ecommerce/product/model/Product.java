package com.abbas.ecommerce.product.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "products")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Double price;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private Integer stock;

    // Her ürünün tek bir ProductImage nesnesi var
    @OneToOne(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private ProductImage image;

    public void setImage(ProductImage image) {
        this.image = image;
        if (image != null) {
            image.setProduct(this); // ilişkiyi ters yönde de kuruyor
        }
    }

}
