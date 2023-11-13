package com.example.iphoneStore.repository;

import com.example.iphoneStore.model.Order;
import com.example.iphoneStore.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends CrudRepository<Order, Long> {
    Optional<Order> findByUser(User user);

    List<Order> findByPaidFalseAndDateCreatedBefore(LocalDateTime tenMinutesAgo);
}
