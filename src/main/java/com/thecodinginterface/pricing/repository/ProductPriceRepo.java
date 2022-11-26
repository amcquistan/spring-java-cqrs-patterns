package com.thecodinginterface.pricing.repository;

import com.thecodinginterface.pricing.domain.model.ProductPriceEntity;
import org.springframework.data.repository.CrudRepository;

public interface ProductPriceRepo extends CrudRepository<ProductPriceEntity, Long> {
}
