package com.thecodinginterface.pricing.service;

import com.thecodinginterface.pricing.domain.model.ProductPriceEntity;
import com.thecodinginterface.pricing.domain.model.ProductPricingEntity;
import com.thecodinginterface.pricing.repository.ProductPriceRepo;
import com.thecodinginterface.pricing.repository.ProductPricingRepo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;


@Service
public class ProductPriceService {

    private ProductPriceRepo productPriceRepo;
    private ProductPricingRepo productPricingRepo;

    public ProductPriceService(ProductPriceRepo productPriceRepo, ProductPricingRepo productPricingRepo) {
        this.productPriceRepo = productPriceRepo;
        this.productPricingRepo  = productPricingRepo;
    }

    public ProductPriceEntity fetchProductPrice(long productId) {
        var id = Long.toString(productId);
        return productPriceRepo.findById(id).orElseGet(() -> {
            var price = fetchPriceFromPricing(productId);
            return productPriceRepo.save(price);
        });
    }

    public ProductPriceEntity updatePriceFromLatestPricing(ProductPricingEntity pricing) {

        ProductPriceEntity currPrice = fetchPriceFromPricing(pricing.getProductId());
        if (currPrice.getEnd() == null) {
            currPrice.setEnd(pricing.getStart());
        }

        productPriceRepo.save(currPrice);
        return currPrice;
    }

    private ProductPriceEntity fetchPriceFromPricing(long productId) {
        var currentAndNextPrice = loadCurrentAndNextPrice(productId);

        ProductPriceEntity currPrice = new ProductPriceEntity();
        currPrice.setPrice(currentAndNextPrice.getCurrentPrice().getPrice());
        currPrice.setProductId(
                Long.toString(currentAndNextPrice.getCurrentPrice().getProductId())
        );

        if (currentAndNextPrice.getNextPrice() != null) {
            currPrice.setEnd(currentAndNextPrice.getNextPrice().getStart());
        }
        return currPrice;
    }

    private CurrentAndNextPrice loadCurrentAndNextPrice(long productId) {
        ProductPricingEntity currPrice = null;
        ProductPricingEntity nextPrice = null;
        var now = OffsetDateTime.now();

        var prices = productPricingRepo.findByProductIdOrderByStartDesc(productId);
        for (ProductPricingEntity price : prices) {
            if (price.getStart().isBefore(now)) {
                currPrice = price;
                break;
            }
            nextPrice = price;
        }

        return new CurrentAndNextPrice(
                currPrice,
                nextPrice
        );
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @ToString
    static final class CurrentAndNextPrice {
        private ProductPricingEntity currentPrice;
        private ProductPricingEntity nextPrice;
    }
}
