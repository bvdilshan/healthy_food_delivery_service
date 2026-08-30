package com.healthyfood.hfapi.controller;

import com.healthyfood.hfapi.dto.FoodRequest;
import com.healthyfood.hfapi.dto.FoodResponse;
import com.healthyfood.hfapi.service.FoodService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import tools.jackson.databind.DatabindException;
import tools.jackson.databind.ObjectMapper;

import java.util.List;

@RestController
@RequestMapping("/api/foods")
@AllArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class FoodController {

    private final ObjectMapper objectMapper;
    private final FoodService foodService;

    @PostMapping
    public ResponseEntity<FoodResponse> addFood(
            @Valid @RequestPart("food") String foodString,
            @RequestPart("file") MultipartFile file) {
        FoodRequest request;
        try {
            request = objectMapper.readValue(foodString, FoodRequest.class);
        } catch (DatabindException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid JSON format for food request", e);
        }

        FoodResponse response = foodService.addFood(request, file);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    @GetMapping
    public List<FoodResponse> getFoods(){
        return foodService.getFoods();


    }
    @GetMapping("/{id}")
    public FoodResponse getFood(@PathVariable String id){
       return foodService.getFood(id);

    }
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteFood(@PathVariable String id){
        foodService.deleteFood(id);

    }
    @PutMapping("/{id}")
    public ResponseEntity<FoodResponse> updateFood(
            @PathVariable String id,
            @Valid @RequestPart("food") String foodString,
            @RequestPart(value = "file", required = false) MultipartFile file) {

        FoodRequest request;
        try {
            request = objectMapper.readValue(foodString, FoodRequest.class);
        } catch (DatabindException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid JSON format for food update", e);
        }

        FoodResponse response = foodService.updateFood(id, request, file);
        return ResponseEntity.ok(response);
    }
    @GetMapping("/paginated")
    public ResponseEntity<Page<FoodResponse>> getFoodsPaginated(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) Double maxPrice,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "id") String sortBy
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy).ascending());
        Page<FoodResponse> response = foodService.getFoodsPaginated(search, maxPrice, pageable);
        return ResponseEntity.ok(response);
    }
}