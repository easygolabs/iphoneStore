package com.example.iphoneStore.repository;

import com.example.iphoneStore.model.Goods;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface GoodsRepository extends CrudRepository<Goods, Long> {
    Optional<Goods> findByName(String name);
}