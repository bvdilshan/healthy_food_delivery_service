package com.healthyfood.hfapi.service;

import com.healthyfood.hfapi.dto.OrderRequest;
import com.healthyfood.hfapi.dto.OrderResponse;
import java.util.List;

public interface OrderService {
    OrderResponse createOrder(OrderRequest request, String userEmail);
    List<OrderResponse> getOrdersForUser(String userEmail);
    List<OrderResponse> getAllOrders();
    OrderResponse updateOrderStatus(String orderId, String status);
}