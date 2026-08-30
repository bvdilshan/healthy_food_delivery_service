package com.healthyfood.hfapi.controller;

import com.healthyfood.hfapi.dto.OrderRequest;
import com.healthyfood.hfapi.dto.OrderResponse;
import com.healthyfood.hfapi.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(
            @RequestBody OrderRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {

        String email = userDetails.getUsername();
        OrderResponse response = orderService.createOrder(request, email);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/my-orders")
    public ResponseEntity<List<OrderResponse>> getMyOrders(
            @AuthenticationPrincipal UserDetails userDetails) {

        String email = userDetails.getUsername();
        List<OrderResponse> orders = orderService.getOrdersForUser(email);
        return ResponseEntity.ok(orders);
    }

    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<List<OrderResponse>> getAllOrders() {
        List<OrderResponse> orders = orderService.getAllOrders();
        return ResponseEntity.ok(orders);
    }

    @PatchMapping("/{id}/status")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<OrderResponse> updateOrderStatus(
            @PathVariable String id,
            @RequestParam String status) {

        OrderResponse response = orderService.updateOrderStatus(id, status);
        return ResponseEntity.ok(response);
    }
}