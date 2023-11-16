package com.example.iphoneStore.service;

import com.example.iphoneStore.dto.AddedGoods;
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

    public Goods addGoods(AddedGoods newGoods) {
        Optional<Goods> existingGoods = goodsRepo.findByName(newGoods.getName());

        if (existingGoods.isPresent()) {
            throw new IllegalArgumentException("Goods with the same name already exist.");
        }

        return goodsRepo.save(
                new Goods(newGoods.getName(), newGoods.getPrice(), newGoods.getQuantity())
        );
    }

    public List<Goods> getAllGoods() {
        return (List<Goods>) goodsRepo.findAll();
    }
}
