package com.ecommerce.smartphoneshop.CUSTOMER;

import com.ecommerce.smartphoneshop.domain.*;
import com.ecommerce.smartphoneshop.repository.CartItemRepository;
import com.ecommerce.smartphoneshop.repository.OrderLineRepository;
import com.ecommerce.smartphoneshop.repository.ProductItemRepository;
import com.ecommerce.smartphoneshop.repository.UserRepository;
import com.ecommerce.smartphoneshop.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
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
    private final CartItemRepository cartItemRepository;
    private final UserRepository userRepository;
    private final ProductItemService productItemService;
    private final CartService cartService;
    private final CartItemService cartItemService;
    private final OrderService orderService;
    private final OrderLineService orderLineService;
    private final ProductItemRepository productItemRepository;

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
            // Đã có sản phẩm trong giỏ hàng
            if (existingCartItem.getProductItem() != null) {
                existingCartItem.setQty(existingCartItem.getQty() + qty);
                // Thêm quá số lượng
                if (existingCartItem.getQty() > productItem.getQty_in_stock()) {
                    redirectAttributes.addFlashAttribute("error", "Không thể thêm quá số lượng sản phẩm còn lại!");
                } else {
                    cartItemRepository.save(existingCartItem);
                    redirectAttributes.addFlashAttribute("success", "Thêm sản phẩm vào giỏ hàng thành công!");
                }
            } else {
                // Thêm quá số lượng
                if (qty > productItem.getQty_in_stock()) {
                    redirectAttributes.addFlashAttribute("error", "Không thể thêm quá số lượng sản phẩm còn lại!");
                } else {
                    cartItemService.saveItem(shoppingCart, productItem, qty);
                    redirectAttributes.addFlashAttribute("success", "Thêm sản phẩm vào giỏ hàng thành công!");
                }
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Có lỗi xảy ra!");
        }
        return "redirect:/product/" + productItem.getProduct().getName() + "/" + itemId;
    }

    @RequestMapping("/update-cart-item/{itemId}")
    public String updateCartItem(@PathVariable Long itemId,
                                 @RequestParam("qty") int qty,
                                 RedirectAttributes redirectAttributes) {
        try {
            ShoppingCartItem cartItem = cartItemService.findById(itemId);
            if (cartItemService.updateItem(cartItem, qty) != null) {
                redirectAttributes.addFlashAttribute("success","Cập nhật giỏ hàng thành công!");
            } else {
               redirectAttributes.addFlashAttribute("error", "Không thể thêm quá số lượng sản phẩm còn lại!");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Có lỗi xảy ra!");
        }
        return "redirect:/cart";
    }

    @RequestMapping("/delete-cart-item")
    public String deleteItem(@RequestParam("id") Long id, RedirectAttributes redirectAttributes) {
        cartItemService.deleteItem(id);
        redirectAttributes.addFlashAttribute("success", "Xoá sản phẩm khỏi giỏ hàng thành công!");
        return "redirect:/cart";
    }

    @GetMapping("/checkout")
    public String checkout(Model model, Principal principal) {
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

        model.addAttribute("user", user);
        model.addAttribute("cartItems", cartItems);
        model.addAttribute("total", total);
        model.addAttribute("order", new ShopOrder());

        return "user-checkout";
    }

    @PostMapping("/place-order")
    public String createOrder(@RequestParam("address") String address,
                              @RequestParam("paymentMethod") PaymentMethod paymentMethod,
                              @RequestParam("total") Long total,
                              Principal principal,
                              RedirectAttributes redirectAttributes) {
        if (principal == null) {
            return "redirect:/login";
        }
        User user = userRepository.findByUsername(principal.getName());
        ShopOrder order = orderService.saveOrder(user, address, PaymentMethod.CASH, total, OrderStatus.CREATED);
        try {
            List<ShoppingCartItem> cartItems = cartItemService.getCartItems(user.getShoppingCart().getId());
            for (ShoppingCartItem item : cartItems) {
                orderLineService.saveOrderLine(item.getProductItem(), order, item.getQty(), item.getProductItem().getPrice());
                item.getProductItem().setQty_in_stock(item.getProductItem().getQty_in_stock() - item.getQty());
                productItemRepository.save(item.getProductItem());
            }
            cartItemService.deleteUserCart(user.getId());

            redirectAttributes.addFlashAttribute("success", "Đơn hàng được tạo thành công!");
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Có lỗi xảy ra!");
        }
        return "redirect:/confirmation/" + order.getId();
    }

    @GetMapping("/confirmation/{id}")
    public String confirmation(@PathVariable("id") Long orderId,
                               Model model, Principal principal) {
        if (principal == null) {
            return "redirect:/login";
        }
        User user = userRepository.findByUsername(principal.getName());
        ShopOrder order = orderService.findOrderById(orderId);
        if (order.getUser() == user) {
            List<OrderLine> items = orderLineService.getOrderLinesByOrderId(order.getId());
            model.addAttribute("order", order);
            model.addAttribute("items", items);
            return "user-order-confirmation";
        } else {
            return "user-404";
        }
    }
}
