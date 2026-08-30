package com.healthyfood.hfapi.service;

import com.healthyfood.hfapi.dto.FoodRequest;
import com.healthyfood.hfapi.dto.FoodResponse;
import org.springframework.web.multipart.MultipartFile;

import org.springframework.data.domain.Pageable;
import java.util.List;
import org.springframework.data.domain.Page;


public interface FoodService {

    String uploadFile(MultipartFile file);
    FoodResponse addFood(FoodRequest request, MultipartFile file);
    List<FoodResponse> getFoods();
    FoodResponse getFood(String id);
    boolean deleteFile(String filename);
    void deleteFood(String id);
    FoodResponse updateFood(String id, FoodRequest request, MultipartFile file);
    Page<FoodResponse> getFoodsPaginated(String search, Double maxPrice, Pageable pageable);

}
