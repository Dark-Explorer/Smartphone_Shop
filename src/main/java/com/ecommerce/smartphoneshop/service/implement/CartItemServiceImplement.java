package com.ecommerce.smartphoneshop.service.implement;

import com.ecommerce.smartphoneshop.domain.ProductItem;
import com.ecommerce.smartphoneshop.domain.ShoppingCart;
import com.ecommerce.smartphoneshop.domain.ShoppingCartItem;
import com.ecommerce.smartphoneshop.repository.CartItemRepository;
import com.ecommerce.smartphoneshop.service.CartItemService;
import com.ecommerce.smartphoneshop.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartItemServiceImplement implements CartItemService {
    private final CartItemRepository cartItemRepository;
    private final CartService cartService;

    @Autowired
    public CartItemServiceImplement(CartItemRepository cartItemRepository, CartService cartService) {
        this.cartItemRepository = cartItemRepository;
        this.cartService = cartService;
    }

    @Override
    public ShoppingCartItem findById(Long itemId) {
        return cartItemRepository.findById(itemId).orElse(null);
    }

    @Override
    public List<ShoppingCartItem> getCartItems(Long cartId) {
        return cartItemRepository.getItemsOfCart(cartId);
    }

    @Override
    public ShoppingCartItem saveItem(ShoppingCart shoppingCart, ProductItem productItem, int qty) {
        ShoppingCartItem shoppingCartItem = ShoppingCartItem.builder()
                .shoppingCart(shoppingCart)
                .productItem(productItem)
                .qty(qty)
                .build();
        return cartItemRepository.save(shoppingCartItem);
    }

    @Override
    public ShoppingCartItem updateItem(ShoppingCartItem shoppingCartItem, int qty) {
        if (qty <= shoppingCartItem.getProductItem().getQty_in_stock() && qty > 0) {
            shoppingCartItem.setQty(qty);
            return cartItemRepository.save(shoppingCartItem);
        } else return null;
    }

    @Override
    public ShoppingCartItem getExistingItem(ShoppingCart shoppingCart, ProductItem productItem) {
        ShoppingCartItem existingItem = new ShoppingCartItem();
        for (ShoppingCartItem item : cartItemRepository.findAll()) {
            if (item.getProductItem().equals(productItem)) {
                existingItem = item;
                break;
            }
        }
        return existingItem;
    }

    @Override
    public void deleteItem(Long itemId) {
        cartItemRepository.deleteById(itemId);
    }

    @Override
    public void deleteUserCart(Long userId) {
        ShoppingCart cart = cartService.getUserCart(userId);
        cartItemRepository.deleteAll(cart.getShoppingCartItems());
    }
}
