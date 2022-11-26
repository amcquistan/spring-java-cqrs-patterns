package com.thecodinginterface.pricing.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductPriceEntity {
    private long productId;

    private BigDecimal price;

    private OffsetDateTime end;
}
