package com.example.iphoneStore.service;

import com.example.iphoneStore.model.Order;
import com.example.iphoneStore.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class DeleteUnpaidOrdersTask {

    private static final int MINUTE_IN_MILLIS = 60000;

    @Autowired
    private OrderRepository orderRepository;

    @Scheduled(fixedRate = MINUTE_IN_MILLIS * 10)
    public void deleteUnpaidOrders() {
        LocalDateTime tenMinutesAgo = LocalDateTime.now().minusMinutes(10);
        List<Order> unpaidOrders = orderRepository.findByPaidFalseAndDateCreatedBefore(tenMinutesAgo);

        if (!unpaidOrders.isEmpty()) {
            for (Order order : unpaidOrders) {
                order.getGoods().clear();
                orderRepository.save(order);
            }
            orderRepository.deleteAll(unpaidOrders);
        }
    }
}