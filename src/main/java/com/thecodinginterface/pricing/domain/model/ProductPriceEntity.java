package com.thecodinginterface.pricing.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@RedisHash("price")
@ToString
public class ProductPriceEntity {
    @Id
    private String productId;

    private BigDecimal price;

    private OffsetDateTime end;
}
