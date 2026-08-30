package com.healthyfood.hfapi.repo;

import com.healthyfood.hfapi.model.FoodModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;



public interface FoodRepository extends MongoRepository<FoodModel,String> {
    Page<FoodModel> findByNameContainingIgnoreCase(String name, Pageable pageable);
    Page<FoodModel> findByPriceLessThanEqual(Double maxPrice, Pageable pageable);
}
