package com.ecommerce.smartphoneshop.service;

import com.ecommerce.smartphoneshop.domain.ShoppingCart;
import org.springframework.stereotype.Service;

@Service
public interface CartService {
    ShoppingCart getUserCart(Long userId);
}
