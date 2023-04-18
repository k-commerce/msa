package com.kcommerce.domain.order.controller;

import com.kcommerce.domain.order.dto.OrderDto;
import com.kcommerce.domain.order.dto.OrderItemDto;
import com.kcommerce.domain.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/api/orders")
    public ResponseEntity<Void> createOrder(@RequestBody @Valid OrderDto.Request request,
                                            @RequestHeader("X-Authorization-Id") Long memberId) {
        orderService.createOrder(request, memberId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/api/orders")
    public ResponseEntity<List<OrderDto>> getOrderHistory(@RequestHeader("X-Authorization-Id") Long memberId) {
        return ResponseEntity.ok(orderService.getOrderHistory(memberId));
    }

    @PutMapping("/api/orders/{orderId}")
    public ResponseEntity<Void> updateOrderItemStatus(@PathVariable Long orderId,
                                                      @RequestBody OrderItemDto.Request request,
                                                      @RequestHeader("X-Authorization-Id") Long memberId) {
        orderService.updateOrderItemStatus(memberId, orderId, request.getId());
        return ResponseEntity.ok().build();
    }
}
