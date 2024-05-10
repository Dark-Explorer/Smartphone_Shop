package com.ecommerce.smartphoneshop.service;

import com.ecommerce.smartphoneshop.domain.ProductItem;
import com.ecommerce.smartphoneshop.domain.ShoppingCart;
import com.ecommerce.smartphoneshop.domain.ShoppingCartItem;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CartItemService {
    ShoppingCartItem findById(Long itemId);
    List<ShoppingCartItem> getCartItems(Long cartId);
    ShoppingCartItem saveItem(ShoppingCart shoppingCart, ProductItem productItem, int qty);
    ShoppingCartItem updateItem(ShoppingCartItem shoppingCartItem, int qty);
    ShoppingCartItem getExistingItem(ShoppingCart shoppingCart, ProductItem productItem);
    void deleteItem(Long itemId);

}
