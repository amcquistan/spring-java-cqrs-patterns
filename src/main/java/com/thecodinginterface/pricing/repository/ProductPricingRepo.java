package com.thecodinginterface.pricing.repository;

import com.thecodinginterface.pricing.domain.model.ProductPricingEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductPricingRepo extends CrudRepository<ProductPricingEntity, Long> {

    List<ProductPricingEntity> findByProductIdOrderByStartDesc(long productId);
}
