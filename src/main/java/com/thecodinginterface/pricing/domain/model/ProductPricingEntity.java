package com.thecodinginterface.pricing.domain.model;


import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Entity
@Table(name = "product_pricing")
@Data
@NoArgsConstructor
public class ProductPricingEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "product_id")
    private long productId;

    @Column(precision = 7, scale = 2)
    private BigDecimal price;

    @Column(columnDefinition = "TIMESTAMP")
    private OffsetDateTime start;

    public ProductPricingEntity(long productId, BigDecimal price, OffsetDateTime start) {
        this.productId = productId;
        this.price = price;
        this.start = start;
    }
}
