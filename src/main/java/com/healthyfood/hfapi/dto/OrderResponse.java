package com.healthyfood.hfapi.dto;

import com.healthyfood.hfapi.model.OrderItem;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder

public class OrderResponse {
    private String id;
    private String userEmail;
    private List<OrderItem> items;
    private Double totalPrice;
    private String status;
    private String orderDate;
}