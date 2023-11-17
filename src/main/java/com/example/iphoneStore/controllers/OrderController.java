package com.example.iphoneStore.controllers;

import com.example.iphoneStore.dto.OrderedGoods;
import com.example.iphoneStore.exceptions.*;
import com.example.iphoneStore.model.Order;
import com.example.iphoneStore.service.OrderService;
import com.fasterxml.jackson.databind.exc.ValueInstantiationException;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

import static com.example.iphoneStore.handlers.GlobalExceptionHandler.ERROR_MESSAGE;

@RestController
@RequestMapping("/api/orders")
@AllArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @ExceptionHandler({GoodsNotFoundException.class, OrderNotFoundException.class, UserNotFoundException.class})
    public ResponseEntity<?> handleGoodsNotFound(RuntimeException ex) {
        return new ResponseEntity<>(Collections.singletonMap(ERROR_MESSAGE, ex.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({InsufficientQuantityException.class, OrderAlreadyPaidException.class})
    public ResponseEntity<?> handleInsufficientQuantity(RuntimeException ex) {
        return new ResponseEntity<>(Collections.singletonMap(ERROR_MESSAGE, ex.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ValueInstantiationException.class)
    public ResponseEntity<String> handleInvalidJsonFormat() {
        return ResponseEntity.badRequest().body("The goods id AND quantity field cannot be null!");
    }

    @PostMapping("/")
    public ResponseEntity<Order> placeOrder(
            @RequestBody List<OrderedGoods> orderedGoods,
            @RequestParam Long userId
    ) {
        Order newOrder = orderService.createOrder(orderedGoods, userId);
        return new ResponseEntity<>(newOrder, HttpStatus.CREATED);
    }

    @PutMapping("/{orderId}/payment")
    public ResponseEntity<String> payForOrder(@PathVariable Long orderId) {
        orderService.payForOrder(orderId);
        return new ResponseEntity<>("Paid successfully.", HttpStatus.OK);
    }
}
