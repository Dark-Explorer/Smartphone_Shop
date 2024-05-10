package com.ecommerce.smartphoneshop.service.implement;

import com.ecommerce.smartphoneshop.domain.ProductItem;
import com.ecommerce.smartphoneshop.domain.ShoppingCart;
import com.ecommerce.smartphoneshop.domain.ShoppingCartItem;
import com.ecommerce.smartphoneshop.domain.User;
import com.ecommerce.smartphoneshop.repository.CartRepository;
import com.ecommerce.smartphoneshop.repository.ProductItemRepository;
import com.ecommerce.smartphoneshop.repository.ProductRepository;
import com.ecommerce.smartphoneshop.repository.UserRepository;
import com.ecommerce.smartphoneshop.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CartServiceImplement implements CartService {

    private final CartRepository cartRepository;
    private final UserRepository userRepository;
    private final ProductItemRepository productItemRepository;

    @Autowired
    public CartServiceImplement(CartRepository cartRepository, UserRepository userRepository, ProductItemRepository productItemRepository) {
        this.cartRepository = cartRepository;
        this.userRepository = userRepository;
        this.productItemRepository = productItemRepository;
    }

    @Override
    public ShoppingCart getUserCart(Long userId) {
        User user = userRepository.findById(userId).orElse(null);
//        return cartRepository.getCartByUserId(userId);
        return user.getShoppingCart();
    }


}
