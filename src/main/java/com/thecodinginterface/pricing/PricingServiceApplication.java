package com.thecodinginterface.pricing;

import com.thecodinginterface.pricing.domain.model.ProductPricingEntity;
import com.thecodinginterface.pricing.service.ProductPriceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.DependsOn;
import org.springframework.kafka.annotation.KafkaListener;

@SpringBootApplication
@DependsOn("jedisConnectionFactory")
public class PricingServiceApplication {
	static final Logger logger = LoggerFactory.getLogger(PricingServiceApplication.class);

	@Autowired
	private ProductPriceService priceService;

	public static void main(String[] args) {
		SpringApplication.run(PricingServiceApplication.class, args);
	}

	@KafkaListener(topics = "#{'${kafka-topics.pricing}'.split(' ')}", containerFactory = "productPricingListenerFactory")
	public void handle(ProductPricingEntity pricing) {
		logger.info("handing pricing {}", pricing);
		var price = priceService.updatePriceFromLatestPricing(pricing);
		logger.info("updated price {}", price);
	}
}
