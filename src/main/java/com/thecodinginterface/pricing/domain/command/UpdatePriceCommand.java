package com.thecodinginterface.pricing.domain.command;

import lombok.Data;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Data
public class UpdatePriceCommand {

    private long productId;

    private BigDecimal price;

    private OffsetDateTime start;
}
