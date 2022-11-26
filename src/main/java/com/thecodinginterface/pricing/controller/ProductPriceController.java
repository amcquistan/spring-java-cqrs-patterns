package com.thecodinginterface.pricing.controller;

import com.thecodinginterface.pricing.domain.command.UpdatePriceCommand;
import com.thecodinginterface.pricing.domain.model.ProductPriceEntity;
import com.thecodinginterface.pricing.service.ProductPricingService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/prices")
public class ProductPriceController {

    private ProductPricingService svc;

    public ProductPriceController(ProductPricingService svc) {
        this.svc = svc;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void update(@RequestBody UpdatePriceCommand cmd) {
        svc.savePriceUpdate(cmd);
    }

    @GetMapping("/{productId}")
    public ProductPriceEntity fetch(@PathVariable("productId") long productId) {
        return svc.fetchProductPrice(productId);
    }
}
