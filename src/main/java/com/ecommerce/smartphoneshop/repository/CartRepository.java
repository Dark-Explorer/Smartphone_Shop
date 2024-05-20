package com.ecommerce.smartphoneshop.repository;

import com.ecommerce.smartphoneshop.domain.ProductItem;
import com.ecommerce.smartphoneshop.domain.ShoppingCart;
import com.ecommerce.smartphoneshop.domain.ShoppingCartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CartRepository extends JpaRepository<ShoppingCart, Long> {
    @Query("select sc from ShoppingCart sc where sc.user.id = :userId")
    ShoppingCart getCartByUserId(Long userId);


}
