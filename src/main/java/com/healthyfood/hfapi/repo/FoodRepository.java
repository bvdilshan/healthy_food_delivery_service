package com.healthyfood.hfapi.repo;

import com.healthyfood.hfapi.model.FoodModel;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface FoodRepo extends MongoRepository<FoodModel,String> {
}
