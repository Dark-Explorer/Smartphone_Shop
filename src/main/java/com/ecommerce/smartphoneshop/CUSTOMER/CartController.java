package com.ecommerce.smartphoneshop.CUSTOMER;

import com.ecommerce.smartphoneshop.domain.ProductItem;
import com.ecommerce.smartphoneshop.domain.ShoppingCart;
import com.ecommerce.smartphoneshop.domain.ShoppingCartItem;
import com.ecommerce.smartphoneshop.domain.User;
import com.ecommerce.smartphoneshop.repository.CartItemRepository;
import com.ecommerce.smartphoneshop.repository.CartRepository;
import com.ecommerce.smartphoneshop.repository.ProductItemRepository;
import com.ecommerce.smartphoneshop.repository.UserRepository;
import com.ecommerce.smartphoneshop.service.CartItemService;
import com.ecommerce.smartphoneshop.service.CartService;
import com.ecommerce.smartphoneshop.service.ProductItemService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
@Log
public class CartController {
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductItemRepository productItemRepository;
    private final UserRepository userRepository;
    private final ProductItemService productItemService;
    private final CartService cartService;
    private final CartItemService cartItemService;

    @GetMapping("/cart")
    public String showCart(Model model, Principal principal) {
        if (principal == null) {
            return "redirect:/login";
        }

        User user = userRepository.findByUsername(principal.getName());
        ShoppingCart cart = cartService.getUserCart(user.getId());
        List<ShoppingCartItem> cartItems = cartItemService.getCartItems(cart.getId());
        long total = 0L;
        for (ShoppingCartItem item : cartItems) {
            total += item.getProductItem().getPrice() * item.getQty();
        }

        model.addAttribute("total", total);
        model.addAttribute("cartItems", cartItems);
        return "user-cart";
    }

    @RequestMapping("/add-to-cart/{userId}/{itemId}")
    public String addToCart(@PathVariable Long userId, @PathVariable Long itemId,
                            @RequestParam int qty, Principal principal,
                            RedirectAttributes redirectAttributes) {
        if (principal == null) {
            return "redirect:/login";
        }

        ShoppingCart shoppingCart = cartService.getUserCart(userId);
        ProductItem productItem = productItemService.findById(itemId);

        try {
            ShoppingCartItem existingCartItem = cartItemService.getExistingItem(shoppingCart, productItem);
            if (existingCartItem.getProductItem() != null) {
                existingCartItem.setQty(existingCartItem.getQty() + qty);
                if (existingCartItem.getQty() > productItem.getQty_in_stock()) {
                    redirectAttributes.addFlashAttribute("error", "Không thể thêm quá số lượng sản phẩm còn lại!");
                } else cartItemRepository.save(existingCartItem);
            } else
                cartItemService.saveItem(shoppingCart, productItem, qty);

            redirectAttributes.addFlashAttribute("success", "Thêm sản phẩm vào giỏ hàng thành công!");

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Có lỗi xảy ra!");
        }
        return "redirect:/product/" + productItem.getProduct().getName() + "/" + itemId;
    }
}
