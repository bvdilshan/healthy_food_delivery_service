package com.healthyfood.hfapi.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FoodRequest {
    private String name;
    private String description;
    private double price;
    private String category;
    private String imageUrl;
    private double calories;
    private double protein;
    private double carbs;
    private double fat;
    private List<String> dietaryPreferences;
    private List<String> allergens;
    private boolean isAvailable;
    private int preparationTimeMinutes;
    private String portionSize;
}