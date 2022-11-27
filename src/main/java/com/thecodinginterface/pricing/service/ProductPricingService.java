package com.thecodinginterface.pricing.service;

import com.thecodinginterface.pricing.domain.command.UpdatePriceCommand;
import com.thecodinginterface.pricing.domain.model.ProductPriceEntity;
import com.thecodinginterface.pricing.domain.model.ProductPricingEntity;
import com.thecodinginterface.pricing.repository.ProductPricingRepo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

@Service
public class ProductPricingService {

    @Value("${kafka-topics.pricing}")
    private String pricingTopic;

    private ProductPricingRepo pricingRepo;
    private KafkaTemplate<Long, ProductPricingEntity> kafkaTemplate;

    public ProductPricingService(ProductPricingRepo pricingRepo, KafkaTemplate<Long, ProductPricingEntity> kafkaTemplate) {
        this.pricingRepo = pricingRepo;
        this.kafkaTemplate = kafkaTemplate;
    }

    public void savePricingUpdate(UpdatePriceCommand cmd) {
        var pricing = new ProductPricingEntity(
                cmd.getProductId(),
                cmd.getPrice(),
                cmd.getStart()
        );
        pricingRepo.save(pricing);
        kafkaTemplate.send(pricingTopic, pricing.getProductId(), pricing);
        kafkaTemplate.flush();
    }
}
