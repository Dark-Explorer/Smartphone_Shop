package com.ecommerce.smartphoneshop.service;

import com.ecommerce.smartphoneshop.domain.ProductItem;
import com.ecommerce.smartphoneshop.domain.ShoppingCart;
import com.ecommerce.smartphoneshop.domain.ShoppingCartItem;
import com.ecommerce.smartphoneshop.domain.User;
import org.springframework.stereotype.Service;

@Service
public interface CartService {
    ShoppingCart getUserCart(Long userId);
}
