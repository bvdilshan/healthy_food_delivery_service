package com.healthyfood.hfapi.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
    @NotBlank(message = "Food name cannot be blank")
    private String name;
    private String description;
    @NotNull(message = "Price is mandatory")
    @Min(value = 0, message = "Price must be greater than or equal to 0")
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