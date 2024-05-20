package com.ecommerce.smartphoneshop.service.implement;

import com.ecommerce.smartphoneshop.domain.*;
import com.ecommerce.smartphoneshop.repository.OrderRepository;
import com.ecommerce.smartphoneshop.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrderServiceImplement implements OrderService {
    private final OrderRepository orderRepository;

    @Autowired
    public OrderServiceImplement(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public ShopOrder saveOrder(User user, String address, PaymentMethod paymentMethod, Long total, OrderStatus status) {
        ShopOrder order = ShopOrder.builder()
                .user(user)
                .date_created(LocalDateTime.now())
                .date_updated(LocalDateTime.now())
                .address(address)
                .payment_method(paymentMethod)
                .total(total)
                .status(status)
                .build();
        return orderRepository.save(order);
    }

    @Override
    public ShopOrder findOrderById(Long id) {
        return orderRepository.findById(id).orElse(null);
    }
}