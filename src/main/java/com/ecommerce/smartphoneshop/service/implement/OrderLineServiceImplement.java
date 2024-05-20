package com.ecommerce.smartphoneshop.service.implement;

import com.ecommerce.smartphoneshop.domain.OrderLine;
import com.ecommerce.smartphoneshop.domain.ProductItem;
import com.ecommerce.smartphoneshop.domain.ShopOrder;
import com.ecommerce.smartphoneshop.repository.OrderLineRepository;
import com.ecommerce.smartphoneshop.service.OrderLineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderLineServiceImplement implements OrderLineService {
    private final OrderLineRepository orderLineRepository;

    @Autowired
    public OrderLineServiceImplement(OrderLineRepository orderLineRepository) {
        this.orderLineRepository = orderLineRepository;
    }

    @Override
    public OrderLine saveOrderLine(ProductItem productItem, ShopOrder order, int qty, Long price) {
        OrderLine orderLine = OrderLine.builder()
                .productItem(productItem)
                .shopOrder(order)
                .qty(qty)
                .price(price)
                .build();
        return orderLineRepository.save(orderLine);
    }

    @Override
    public List<OrderLine> getOrderLinesByOrderId(Long orderId) {
        return orderLineRepository.getOrderLineByOrderId(orderId);
    }
}
