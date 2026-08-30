package com.healthyfood.hfapi.repo;

import com.healthyfood.hfapi.model.OrderItem;
import com.healthyfood.hfapi.model.OrderModel;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface OrderRepository extends MongoRepository<OrderModel, String> {
    List<OrderModel> findByUserEmail(String userEmail);
}
