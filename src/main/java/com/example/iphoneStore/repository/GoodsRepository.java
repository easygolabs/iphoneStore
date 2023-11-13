package com.example.iphoneStore.repository;

import com.example.iphoneStore.model.Goods;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GoodsRepository extends CrudRepository<Goods, Long> {
    Optional<Goods> findByName(String name);
}