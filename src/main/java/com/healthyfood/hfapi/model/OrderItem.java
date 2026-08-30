package com.healthyfood.hfapi.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderItem {
    private String foodId;
    private String foodName;
    private Double price;
    private Integer quantity;
}