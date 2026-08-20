package com.healthyfood.hfapi.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.List;

@Document(collection = "foods")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FoodModel {
    @Id
    private String id;
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
    private double rating;
    private String portionSize;
}