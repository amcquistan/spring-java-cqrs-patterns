package com.thecodinginterface.pricing.service;

import com.thecodinginterface.pricing.domain.command.UpdatePriceCommand;
import com.thecodinginterface.pricing.domain.model.ProductPriceEntity;
import com.thecodinginterface.pricing.domain.model.ProductPricingEntity;
import com.thecodinginterface.pricing.repository.ProductPricingRepo;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

@Service
public class ProductPricingService {

    private ProductPricingRepo pricingRepo;

    public ProductPricingService(ProductPricingRepo pricingRepo) {
        this.pricingRepo = pricingRepo;
    }

    public void savePriceUpdate(UpdatePriceCommand cmd) {
        pricingRepo.save(new ProductPricingEntity(
                cmd.getProductId(),
                cmd.getPrice(),
                cmd.getStart()
        ));
    }

    public ProductPriceEntity fetchProductPrice(long productId) {
        ProductPriceEntity currPrice = null;
        ProductPricingEntity nextPrice = null;
        var now = OffsetDateTime.now();

        var prices = pricingRepo.findByProductIdOrderByStartDesc(productId);
        for (ProductPricingEntity price : prices) {
            if (price.getStart().isBefore(now)) {
                currPrice = new ProductPriceEntity();
                currPrice.setProductId(price.getProductId());
                currPrice.setPrice(price.getPrice());
                break;
            }
            nextPrice = price;
        }

        if (nextPrice != null) {
            currPrice.setEnd(nextPrice.getStart());
        }

        return currPrice;
    }
}
