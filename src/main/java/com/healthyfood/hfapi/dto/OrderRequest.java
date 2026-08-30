package com.healthyfood.hfapi.dto;

import com.healthyfood.hfapi.model.OrderItem;
import lombok.Data;

import java.util.List;

@Data
public class OrderRequest {
    private List<OrderItemDto> items;
}
