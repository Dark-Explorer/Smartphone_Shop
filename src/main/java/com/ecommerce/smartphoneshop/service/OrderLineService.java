package com.ecommerce.smartphoneshop.service;

import com.ecommerce.smartphoneshop.domain.OrderLine;
import com.ecommerce.smartphoneshop.domain.ProductItem;
import com.ecommerce.smartphoneshop.domain.ShopOrder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface OrderLineService {
    OrderLine saveOrderLine(ProductItem productItem, ShopOrder order, int qty, Long price);
    List<OrderLine> getOrderLinesByOrderId(Long orderId);
}
