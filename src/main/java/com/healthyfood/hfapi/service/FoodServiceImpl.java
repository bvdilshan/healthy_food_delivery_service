package com.healthyfood.hfapi.service;

import com.healthyfood.hfapi.dto.FoodRequest;
import com.healthyfood.hfapi.dto.FoodResponse;
import com.healthyfood.hfapi.model.FoodModel;
import com.healthyfood.hfapi.repo.FoodRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FoodServiceImpl implements FoodService {

    private final FoodRepository foodRepository;
    private final S3Client s3Client;

    @Value("${aws.s3.bucket-name}")
    private String bucketName;

    @Override
    public String uploadFile(MultipartFile file) {
        String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
        try {
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(fileName)
                    .contentType(file.getContentType())
                    .build();

            s3Client.putObject(putObjectRequest,
                    RequestBody.fromBytes(file.getBytes()));

            return String.format("https://%s.s3.amazonaws.com/%s", bucketName, fileName);
        } catch (IOException e) {
            throw new RuntimeException("Failed to upload file to S3", e);
        }
    }

    @Override
    public FoodResponse addFood(FoodRequest request, MultipartFile file) {
        String imageUrl = null;
        if (file != null && !file.isEmpty()) {
            imageUrl = uploadFile(file);
        }

        FoodModel food = FoodModel.builder()
                .name(request.getName())
                .price(request.getPrice())
                .description(request.getDescription())
                .category(request.getCategory())
                .calories(request.getCalories())
                .protein(request.getProtein())
                .carbs(request.getCarbs())
                .fat(request.getFat())
                .dietaryPreferences(request.getDietaryPreferences())
                .allergens(request.getAllergens())
                .isAvailable(request.isAvailable())
                .preparationTimeMinutes(request.getPreparationTimeMinutes())
                .portionSize(request.getPortionSize())
                .imageUrl(imageUrl)
                .build();

        FoodModel savedFood = foodRepository.save(food);
        return mapToResponse(savedFood);
    }

    @Override
    public List<FoodResponse> getFoods() {
        return foodRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public FoodResponse getFood(String id) {
        FoodModel food = foodRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Food not found with id: " + id));
        return mapToResponse(food);
    }

    @Override
    public boolean deleteFile(String fileUrl) {
        try {
            String fileName = fileUrl.substring(fileUrl.lastIndexOf("/") + 1);
            DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                    .bucket(bucketName)
                    .key(fileName)
                    .build();
            s3Client.deleteObject(deleteObjectRequest);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public void deleteFood(String id) {
        FoodModel food = foodRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Food not found"));

        if (food.getImageUrl() != null) {
            deleteFile(food.getImageUrl());
        }

        foodRepository.deleteById(id);
    }


    @Override
    public FoodResponse updateFood(String id, FoodRequest request, MultipartFile file) {
        FoodModel food = foodRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Food not found with id: " + id));

        food.setName(request.getName());
        food.setPrice(request.getPrice());
        food.setDescription(request.getDescription());
        food.setCategory(request.getCategory());
        food.setCalories(request.getCalories());
        food.setProtein(request.getProtein());
        food.setCarbs(request.getCarbs());
        food.setFat(request.getFat());
        food.setDietaryPreferences(request.getDietaryPreferences());
        food.setAllergens(request.getAllergens());
        food.setAvailable(request.isAvailable());
        food.setPreparationTimeMinutes(request.getPreparationTimeMinutes());
        food.setPortionSize(request.getPortionSize());

        if (file != null && !file.isEmpty()) {
            if (food.getImageUrl() != null) {
                deleteFile(food.getImageUrl());
            }
            String newImageUrl = uploadFile(file);
            food.setImageUrl(newImageUrl);
        }

        FoodModel updatedFood = foodRepository.save(food);
        return mapToResponse(updatedFood);
    }
    @Override
    public Page<FoodResponse> getFoodsPaginated(String search, Double maxPrice, Pageable pageable) {
        Page<FoodModel> foodPage;

        if (search != null && !search.trim().isEmpty()) {
            foodPage = foodRepository.findByNameContainingIgnoreCase(search, pageable);
        } else if (maxPrice != null) {
            foodPage = foodRepository.findByPriceLessThanEqual(maxPrice, pageable);
        } else {
            foodPage = foodRepository.findAll(pageable);
        }

        return foodPage.map(this::mapToResponse);
    }

    private FoodResponse mapToResponse(FoodModel food) {
        return FoodResponse.builder()
                .id(food.getId())
                .name(food.getName())
                .price(food.getPrice())
                .description(food.getDescription())
                .imageUrl(food.getImageUrl())
                .category(food.getCategory())
                .calories(food.getCalories())
                .protein(food.getProtein())
                .carbs(food.getCarbs())
                .fat(food.getFat())
                .dietaryPreferences(food.getDietaryPreferences())
                .allergens(food.getAllergens())
                .isAvailable(food.isAvailable())
                .preparationTimeMinutes(food.getPreparationTimeMinutes())
                .portionSize(food.getPortionSize())
                .build();
    }
}