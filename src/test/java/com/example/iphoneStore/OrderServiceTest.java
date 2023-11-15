package com.example.iphoneStore;

import com.example.iphoneStore.dto.OrderedGoods;
import com.example.iphoneStore.emums.Role;
import com.example.iphoneStore.model.Goods;
import com.example.iphoneStore.model.Order;
import com.example.iphoneStore.model.User;
import com.example.iphoneStore.repository.GoodsRepository;
import com.example.iphoneStore.repository.OrderRepository;
import com.example.iphoneStore.repository.UserRepository;
import com.example.iphoneStore.service.OrderService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class OrderServiceTest {

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private GoodsRepository goodsRepository;

    @Test
    void testPlaceOrder() {
        User user = new User();
        user.setUsername("testUser");
        user.setPassword("testPassword");
        user.setRole(Role.CLIENT);
        userRepository.save(user);

        Goods goods = new Goods();
        goods.setName("Test Goods");
        goods.setPrice(100.0);
        goods.setQuantity(10);
        goodsRepository.save(goods);

        List<OrderedGoods> orderedGoodsList = new ArrayList<>();
        orderedGoodsList.add(new OrderedGoods(goods.getId(), 5));

        Order order = orderService.createOrder(orderedGoodsList, user.getId());

        Optional<Order> savedOrder = orderRepository.findById(order.getId());

        assertTrue(savedOrder.isPresent());
        assertEquals(savedOrder.get().getUser().getId(), user.getId());
        assertTrue(savedOrder.get().getGoods().contains(goods));
        assertEquals(1, savedOrder.get().getGoods().size());
    }
}
