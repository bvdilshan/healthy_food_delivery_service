package com.healthyfood.hfapi.service;

import com.healthyfood.hfapi.dto.FoodRequest;
import com.healthyfood.hfapi.dto.FoodResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FoodService {

    String uploadFile(MultipartFile file);
    FoodResponse addFood(FoodRequest request, MultipartFile file);
    List<FoodResponse> getFoods();
    FoodResponse getFood(String id);
    boolean deleteFile(String filename);
    void deleteFood(String id);


}
