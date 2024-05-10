package com.ecommerce.smartphoneshop.service.implement;

import com.ecommerce.smartphoneshop.domain.ProductItem;
import com.ecommerce.smartphoneshop.domain.ShoppingCart;
import com.ecommerce.smartphoneshop.domain.ShoppingCartItem;
import com.ecommerce.smartphoneshop.repository.CartItemRepository;
import com.ecommerce.smartphoneshop.repository.ProductRepository;
import com.ecommerce.smartphoneshop.service.CartItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartItemServiceImplement implements CartItemService {
    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;

    @Autowired
    public CartItemServiceImplement(CartItemRepository cartItemRepository, ProductRepository productRepository) {
        this.cartItemRepository = cartItemRepository;
        this.productRepository = productRepository;
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
        shoppingCartItem.setQty(qty);
        return cartItemRepository.save(shoppingCartItem);
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
}
