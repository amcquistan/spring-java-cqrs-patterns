package com.thecodinginterface.pricing.service;

import com.thecodinginterface.pricing.domain.model.ProductPriceEntity;
import com.thecodinginterface.pricing.repository.ProductPriceRepo;
import org.springframework.stereotype.Service;


@Service
public class ProductPriceService {

    private ProductPriceRepo productPriceRepo;

    public ProductPriceService(ProductPriceRepo productPriceRepo) {
        this.productPriceRepo = productPriceRepo;
    }

    public ProductPriceEntity fetchProductPrice(long productId) {
        return productPriceRepo.findById(productId).get();
    }
}
