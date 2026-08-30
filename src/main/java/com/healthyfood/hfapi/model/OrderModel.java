package com.healthyfood.hfapi.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "orders")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderModel {
    private String id;
    private String userEmail;
    private List<OrderItem> items;
    private Double totalPrice;
    private String status;
    private String orderDate;

}
