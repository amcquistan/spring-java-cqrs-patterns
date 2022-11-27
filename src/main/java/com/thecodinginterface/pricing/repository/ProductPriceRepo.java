package com.thecodinginterface.pricing.repository;

import com.thecodinginterface.pricing.domain.model.ProductPriceEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductPriceRepo extends CrudRepository<ProductPriceEntity, String> {
}
