package com.ecommerce.smartphoneshop.service;

import com.ecommerce.smartphoneshop.domain.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface OrderService {
    ShopOrder saveOrder(User user, String address, PaymentMethod paymentMethod, Long total, OrderStatus status);
    ShopOrder findOrderById(Long id);
    List<ShopOrder> getAllPendingOrders();
    List<ShopOrder> getAllCompletedOrders();
    ShopOrder acceptOrder(Long id);
    ShopOrder cancelOrder(Long id);
    ShopOrder deliverOrder(Long id);
    Long incomeToday();
    Long incomThisWeek();

}
