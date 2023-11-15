package com.example.iphoneStore.controllers;

import com.example.iphoneStore.dto.OrderedGoods;
import com.example.iphoneStore.exceptions.*;
import com.example.iphoneStore.model.Order;
import com.example.iphoneStore.service.OrderService;
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

    @ExceptionHandler(GoodsNotFoundException.class)
    public ResponseEntity<?> handleGoodsNotFound(GoodsNotFoundException ex) {
        return new ResponseEntity<>(Collections.singletonMap(ERROR_MESSAGE, ex.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(OrderNotFoundException.class)
    public ResponseEntity<?> handleOrderNotFound(OrderNotFoundException ex) {
        return new ResponseEntity<>(Collections.singletonMap(ERROR_MESSAGE, ex.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<?> handleUserNotFound(UserNotFoundException ex) {
        return new ResponseEntity<>(Collections.singletonMap(ERROR_MESSAGE, ex.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InsufficientQuantityException.class)
    public ResponseEntity<?> handleInsufficientQuantity(InsufficientQuantityException ex) {
        return new ResponseEntity<>(Collections.singletonMap(ERROR_MESSAGE, ex.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(OrderAlreadyPaidException.class)
    public ResponseEntity<?> handleOrderAlreadyPaid(OrderAlreadyPaidException ex) {
        return new ResponseEntity<>(Collections.singletonMap(ERROR_MESSAGE, ex.getMessage()), HttpStatus.BAD_REQUEST);
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
    public ResponseEntity<String> payForOrder(@PathVariable long orderId) {
        orderService.payForOrder(orderId);
        return new ResponseEntity<>("Paid successfully.", HttpStatus.OK);
    }
}
