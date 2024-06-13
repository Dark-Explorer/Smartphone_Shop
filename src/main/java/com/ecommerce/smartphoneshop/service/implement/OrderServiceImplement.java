package com.ecommerce.smartphoneshop.service.implement;

import com.ecommerce.smartphoneshop.domain.*;
import com.ecommerce.smartphoneshop.repository.OrderRepository;
import com.ecommerce.smartphoneshop.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
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

    @Override
    public List<ShopOrder> getAllPendingOrders() {
        return orderRepository.getPendingOrders();
    }

    @Override
    public List<ShopOrder> getAllCompletedOrders() {
        return orderRepository.getCompletedOrders();
    }

    @Override
    public ShopOrder acceptOrder(Long id) {
        ShopOrder order = orderRepository.getReferenceById(id);
        order.setStatus(OrderStatus.DELIVERING);
        order.setDate_updated(LocalDateTime.now());
        return orderRepository.save(order);
    }

    @Override
    public ShopOrder cancelOrder(Long id) {
        ShopOrder order = orderRepository.getReferenceById(id);
        order.setStatus(OrderStatus.CANCELLED);
        order.setDate_updated(LocalDateTime.now());
        return orderRepository.save(order);
    }

    @Override
    public ShopOrder deliverOrder(Long id) {
        ShopOrder order = orderRepository.getReferenceById(id);
        order.setStatus(OrderStatus.DELIVERED);
        order.setDate_updated(LocalDateTime.now());
        return orderRepository.save(order);
    }

    @Override
    public Long getIncomeToday() {
        LocalDateTime startOfDay = LocalDate.now().atStartOfDay();
        LocalDateTime currentTime = LocalDateTime.now();
        Long res = orderRepository.getIncomeForDateRange(startOfDay, currentTime);
        if (res == null) return 0L;
        return res;
    }

    @Override
    public Long getIncomeThisMonth() {
        LocalDateTime startOfMonth = LocalDate.now().withDayOfMonth(1).atStartOfDay();
        LocalDateTime currentTime = LocalDateTime.now();
        Long res = orderRepository.getIncomeForDateRange(startOfMonth, currentTime);
        if (res == null) return 0L;
        return res;
    }

    @Override
    public int getNumberOfPendingOrders() {
        return orderRepository.getNumberOfPendingOrders();
    }

    @Override
    public int getNumberOfCompletedOrdersToday() {
        LocalDateTime startOfDay = LocalDate.now().atStartOfDay();
        LocalDateTime currentTime = LocalDateTime.now();
        return orderRepository.getCompletedOrdersInRange(startOfDay, currentTime);
    }

    @Override
    public int getNumberOfCompletedOrdersThisMonth() {
        LocalDateTime startOfMonth = LocalDate.now().withDayOfMonth(1).atStartOfDay();
        LocalDateTime currentTime = LocalDateTime.now();
        return orderRepository.getCompletedOrdersInRange(startOfMonth, currentTime);
    }

}
