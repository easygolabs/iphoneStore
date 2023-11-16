package com.example.iphoneStore.controllers;

import com.example.iphoneStore.dto.AddedGoods;
import com.example.iphoneStore.model.Goods;
import com.example.iphoneStore.service.GoodsService;
import com.fasterxml.jackson.databind.exc.ValueInstantiationException;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

import static com.example.iphoneStore.handlers.GlobalExceptionHandler.ERROR_MESSAGE;

@RestController
@RequestMapping("/api/goods")
@AllArgsConstructor
public class GoodsController {

    private final GoodsService goodsService;

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<?> handleDuplicateGoods(IllegalArgumentException ex) {
        return new ResponseEntity<>(Collections.singletonMap(ERROR_MESSAGE, ex.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ValueInstantiationException.class)
    public ResponseEntity<String> handleInvalidJsonFormat() {
        return ResponseEntity.badRequest().body("The name, price OR quantity field cannot be null!");
    }

    @PostMapping("/")
    public ResponseEntity<Goods> addGoods(@RequestBody AddedGoods newGoods) {
        Goods savedGoods = goodsService.addGoods(newGoods);
        return new ResponseEntity<>(savedGoods, HttpStatus.CREATED);
    }

    @GetMapping("/")
    public ResponseEntity<List<Goods>> getAllGoods() {
        List<Goods> goods = goodsService.getAllGoods();
        return new ResponseEntity<>(goods, HttpStatus.OK);
    }
}
