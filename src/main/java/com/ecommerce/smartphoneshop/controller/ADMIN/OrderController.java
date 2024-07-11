package com.ecommerce.smartphoneshop.controller.ADMIN;

import com.ecommerce.smartphoneshop.domain.ShopOrder;
import com.ecommerce.smartphoneshop.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
@Log
public class OrderController {
    private final OrderService orderService;

    @GetMapping("/pending-orders")
    public String pendingOrders(Model model) {
        List<ShopOrder> orders = orderService.getAllPendingOrders();
        model.addAttribute("orders", orders);

        return "admin-orders-pending";
    }

    @GetMapping("/completed-orders")
    public String completedOrders(Model model) {
        List<ShopOrder> orders = orderService.getAllCompletedOrders();
        model.addAttribute("orders", orders);

        return "admin-orders-completed";
    }

    @RequestMapping("/accept-order/{id}")
    public String acceptOrder(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            orderService.acceptOrder(id);
            redirectAttributes.addFlashAttribute("success", "Đơn hàng đã được duyệt!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Có lỗi xảy ra!");
        }
        return "redirect:/pending-orders";
    }

    @RequestMapping("/cancel-order/{id}")
    public String cancelOrder(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            orderService.cancelOrder(id);
            redirectAttributes.addFlashAttribute("success", "Đơn hàng đã được huỷ!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Có lỗi xảy ra!");
        }
        return "redirect:/pending-orders";
    }

    @RequestMapping("/deliver-order/{id}")
    public String deliverOrder(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            orderService.deliverOrder(id);
            redirectAttributes.addFlashAttribute("success", "Cập nhật thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Có lỗi xảy ra!");
        }
        return "redirect:/pending-orders";
    }
}
