package com.healthyfood.hfapi.service;

import com.healthyfood.hfapi.dto.OrderItemDto;
import com.healthyfood.hfapi.dto.OrderRequest;
import com.healthyfood.hfapi.dto.OrderResponse;
import com.healthyfood.hfapi.model.FoodModel;
import com.healthyfood.hfapi.model.OrderItem;
import com.healthyfood.hfapi.model.OrderModel;
import com.healthyfood.hfapi.repo.FoodRepository;
import com.healthyfood.hfapi.repo.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final FoodRepository foodRepository;

    @Override
    public OrderResponse createOrder(OrderRequest request, String userEmail) {
        List<OrderItem> orderItems = new ArrayList<>();
        double totalPrice = 0.0;

        for (OrderItemDto dto : request.getItems()) {
            FoodModel food = foodRepository.findById(dto.getFoodId())
                    .orElseThrow(() -> new RuntimeException("Food not found with id: " + dto.getFoodId()));

            OrderItem item = OrderItem.builder()
                    .foodId(food.getId())
                    .foodName(food.getName())
                    .price(food.getPrice())
                    .quantity(dto.getQuantity())
                    .build();

            orderItems.add(item);
            totalPrice += food.getPrice() * dto.getQuantity();
        }

        String currentDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        OrderModel order = OrderModel.builder()
                .userEmail(userEmail)
                .items(orderItems)
                .totalPrice(totalPrice)
                .status("PENDING")
                .orderDate(currentDate)
                .build();

        OrderModel savedOrder = orderRepository.save(order);
        return mapToResponse(savedOrder);
    }

    @Override
    public List<OrderResponse> getOrdersForUser(String userEmail) {
        return orderRepository.findByUserEmail(userEmail).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<OrderResponse> getAllOrders() {
        return orderRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }
    @Override
    public OrderResponse updateOrderStatus(String orderId, String status) {
        OrderModel order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + orderId));

        order.setStatus(status.toUpperCase());

        OrderModel updatedOrder = orderRepository.save(order);
        return mapToResponse(updatedOrder);
    }

    private OrderResponse mapToResponse(OrderModel order) {
        return OrderResponse.builder()
                .id(order.getId())
                .userEmail(order.getUserEmail())
                .items(order.getItems())
                .totalPrice(order.getTotalPrice())
                .status(order.getStatus())
                .orderDate(order.getOrderDate())
                .build();
    }
}