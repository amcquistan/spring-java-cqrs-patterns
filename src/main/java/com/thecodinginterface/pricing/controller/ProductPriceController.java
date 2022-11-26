package com.thecodinginterface.pricing.controller;

import com.thecodinginterface.pricing.domain.command.UpdatePriceCommand;
import com.thecodinginterface.pricing.domain.model.ProductPriceEntity;
import com.thecodinginterface.pricing.service.ProductPriceService;
import com.thecodinginterface.pricing.service.ProductPricingService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/prices")
public class ProductPriceController {

    private ProductPricingService productPricingService;
    private ProductPriceService productPriceService;

    public ProductPriceController(ProductPricingService productPricingService, ProductPriceService productPriceService) {
        this.productPricingService = productPricingService;
        this.productPriceService = productPriceService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void update(@RequestBody UpdatePriceCommand cmd) {
        productPricingService.savePriceUpdate(cmd);
    }

    @GetMapping("/{productId}")
    public ProductPriceEntity fetch(@PathVariable("productId") long productId) {
        return productPriceService.fetchProductPrice(productId);
    }
}
