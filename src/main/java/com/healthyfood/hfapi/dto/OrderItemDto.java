package com.healthyfood.hfapi.dto;

import lombok.Data;

@Data
public class OrderItemDto {
    private String foodId;
    private int quantity;

}
