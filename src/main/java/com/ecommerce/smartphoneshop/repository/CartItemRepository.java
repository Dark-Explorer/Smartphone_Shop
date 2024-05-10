package com.ecommerce.smartphoneshop.repository;

import com.ecommerce.smartphoneshop.domain.ProductItem;
import com.ecommerce.smartphoneshop.domain.ShoppingCart;
import com.ecommerce.smartphoneshop.domain.ShoppingCartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartItemRepository extends JpaRepository<ShoppingCartItem, Long> {
    @Query("select sci from ShoppingCartItem sci where sci.shoppingCart.id = :cartId")
    List<ShoppingCartItem> getItemsOfCart(Long cartId);
}
