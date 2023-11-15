package com.example.iphoneStore.service;

import com.example.iphoneStore.model.Goods;
import com.example.iphoneStore.repository.GoodsRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class GoodsService {

    private final GoodsRepository goodsRepo;

    public Goods addGoods(Goods newGoods) {
        Optional<Goods> existingGoods = goodsRepo.findByName(newGoods.getName());
        if (existingGoods.isPresent()) {
            throw new IllegalArgumentException("Goods with the same name already exist.");
        }
        return goodsRepo.save(newGoods);
    }

    public List<Goods> getAllGoods() {
        return (List<Goods>) goodsRepo.findAll();
    }
}
