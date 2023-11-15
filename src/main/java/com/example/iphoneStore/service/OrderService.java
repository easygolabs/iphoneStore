package com.example.iphoneStore.service;

import com.example.iphoneStore.dto.OrderedGoods;
import com.example.iphoneStore.exceptions.*;
import com.example.iphoneStore.model.Goods;
import com.example.iphoneStore.model.Order;
import com.example.iphoneStore.model.User;
import com.example.iphoneStore.repository.GoodsRepository;
import com.example.iphoneStore.repository.OrderRepository;
import com.example.iphoneStore.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class OrderService {

    private final GoodsRepository goodsRepository;
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;

    public Order createOrder(List<OrderedGoods> orders, Long userId) {
        Order order = new Order();
        order.setUser(getUser(userId));

        for (OrderedGoods orderedGoods : orders) {
            Long goodsId = orderedGoods.getId();
            Goods goods = goodsRepository.findById(goodsId).orElseThrow(() -> new GoodsNotFoundException(goodsId));

            if (goods.getQuantity() < orderedGoods.getQuantity()) {
                throw new InsufficientQuantityException(goodsId);
            }

            goods.setQuantity(goods.getQuantity() - orderedGoods.getQuantity());
            order.getGoods().add(goods);
        }
        return orderRepository.save(order);
    }

    public void payForOrder(long orderId) {
        Order existingOrder = orderRepository.findById(orderId).orElseThrow(() -> new OrderNotFoundException(orderId));
        if (existingOrder.isPaid()) {
            throw new OrderAlreadyPaidException(orderId);
        }
        existingOrder.setPaid(true);
        orderRepository.save(existingOrder);
    }

    private User getUser(Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
    }
}
