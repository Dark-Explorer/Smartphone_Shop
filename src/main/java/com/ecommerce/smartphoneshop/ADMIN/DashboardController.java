package com.ecommerce.smartphoneshop.ADMIN;

import com.ecommerce.smartphoneshop.service.OrderService;
import com.ecommerce.smartphoneshop.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
@Log
public class DashboardController {
    private final OrderService orderService;
    private final UserService userService;

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        int numPendingOrders = orderService.getNumberOfPendingOrders();
        Long incomeToday = orderService.getIncomeToday();
        Long incomeThisMonth = orderService.getIncomeThisMonth();
        int newUsers = userService.getNewUsersThisMonth();
        int completedOrdersToday = orderService.getNumberOfCompletedOrdersToday();
        int completedOrdersThisMonth = orderService.getNumberOfCompletedOrdersThisMonth();

        model.addAttribute("numPending", numPendingOrders);
        model.addAttribute("incomeToday", incomeToday);
        model.addAttribute("incomeThisMonth", incomeThisMonth);
        model.addAttribute("newUsers", newUsers);
        model.addAttribute("completedOrdersToday", completedOrdersToday);
        model.addAttribute("completedOrdersThisMonth", completedOrdersThisMonth);

        return "admin-dashboard";
    }


}
