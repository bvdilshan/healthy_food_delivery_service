package com.healthyfood.hfapi.service;

import com.healthyfood.hfapi.dto.FoodRequest;
import com.healthyfood.hfapi.dto.FoodResponse;
import com.healthyfood.hfapi.model.FoodModel;
import com.healthyfood.hfapi.repo.FoodRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.ObjectCannedACL;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FoodServiceIMPL implements FoodService {

    private final S3Client s3Client;
    private final FoodRepo foodRepo;

    @Value("${aws.s3.bucketname}")
    private String bucketName;

    @Override
    public String uploadFile(MultipartFile file) {
        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null || !originalFilename.contains(".")) {
            throw new IllegalArgumentException("Invalid file name");
        }

        String fileNameExtension = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
        String key = UUID.randomUUID().toString() + "." + fileNameExtension;

        try {
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(key)
                    .acl(ObjectCannedACL.PUBLIC_READ)
                    .build();

            s3Client.putObject(putObjectRequest, RequestBody.fromInputStream(file.getInputStream(), file.getSize()));

            return key;
        } catch (IOException e) {
            throw new RuntimeException("Failed to upload file to S3", e);
        }
    }

    @Override
    public FoodResponse addFood(FoodRequest request, MultipartFile file) {
        FoodModel newFoodEntity = convertToEntity(request);
        String imageUrl = uploadFile(file);
        newFoodEntity.setImageUrl(imageUrl);
        newFoodEntity = foodRepo.save(newFoodEntity);
        return convertToResponse(newFoodEntity);
    }



    @Override
    public List<FoodResponse> getFoods() {
       List<FoodModel> databaseEntries = foodRepo.findAll();
       return databaseEntries.stream().map(object -> convertToResponse(object)).collect(Collectors.toList());
    }

    @Override
    public FoodResponse getFood(String id) {
        FoodModel existingFood = foodRepo.findById(id).orElseThrow(() -> new RuntimeException("Food Not Found!"+id));
        return convertToResponse(existingFood);
    }

    @Override
    public boolean deleteFile(String filename) {
        DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                .bucket(bucketName)
                .key(filename)
                .build();
        s3Client.deleteObject(deleteObjectRequest);
        return true;
    }

    @Override
    public void deleteFood(String id) {
        FoodResponse response = getFood(id);
        String imageUrl = response.getImageUrl();
        String filename = imageUrl.substring(imageUrl.lastIndexOf("/")+1);
        boolean isFileDeleted = deleteFile(filename);
        if (isFileDeleted){
            foodRepo.deleteById(response.getId());
        }
    }

    private FoodModel convertToEntity(FoodRequest request) {
        return FoodModel.builder()
                .name(request.getName())
                .description(request.getDescription())
                .category(request.getCategory())
                .price(request.getPrice())
                .build();
    }

    private FoodResponse convertToResponse(FoodModel entity) {
        return FoodResponse.builder()
                .id(entity.getId())
                .name(entity.getName())
                .description(entity.getDescription())
                .category(entity.getCategory())
                .price(entity.getPrice())
                .imageUrl(entity.getImageUrl())
                .build();
    }

}