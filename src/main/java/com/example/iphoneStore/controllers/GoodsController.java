package com.example.iphoneStore.controllers;

import com.example.iphoneStore.model.Goods;
import com.example.iphoneStore.service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

import static com.example.iphoneStore.handlers.GlobalExceptionHandler.ERROR_MESSAGE;

@RestController
@RequestMapping("/api/goods")
public class GoodsController {

    @Autowired
    private GoodsService goodsService;

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<?> handleDuplicateGoods(IllegalArgumentException ex) {
        return new ResponseEntity<>(Collections.singletonMap(ERROR_MESSAGE, ex.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/")
    public ResponseEntity<Goods> addGoods(@RequestBody Goods newGoods) {
        Goods savedGoods = goodsService.addGoods(newGoods);
        return new ResponseEntity<>(savedGoods, HttpStatus.CREATED);
    }

    @GetMapping("/")
    public ResponseEntity<List<Goods>> getAllGoods() {
        List<Goods> goods = goodsService.getAllGoods();
        return new ResponseEntity<>(goods, HttpStatus.OK);
    }
}