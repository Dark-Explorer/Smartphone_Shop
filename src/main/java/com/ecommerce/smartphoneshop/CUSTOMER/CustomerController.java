package com.ecommerce.smartphoneshop.CUSTOMER;

import com.ecommerce.smartphoneshop.domain.Brand;
import com.ecommerce.smartphoneshop.domain.Product;
import com.ecommerce.smartphoneshop.domain.ProductItem;
import com.ecommerce.smartphoneshop.domain.User;
import com.ecommerce.smartphoneshop.dto.ProductDTO;
import com.ecommerce.smartphoneshop.repository.UserRepository;
import com.ecommerce.smartphoneshop.service.BrandService;
import com.ecommerce.smartphoneshop.service.ProductItemService;
import com.ecommerce.smartphoneshop.service.ProductService;
import com.ecommerce.smartphoneshop.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@Log
public class CustomerController {
    private final ProductService productService;
    private final ProductItemService productItemService;
    private final UserService userService;
    private final UserRepository userRepository;
    private final BrandService brandService;

    @GetMapping("/home")
    public String showHome(Model model) {
        List<Product> products = productService.getAllProducts();
        List<ProductDTO> productDTOs = new ArrayList<>();
        for (Product product : products) {
            ProductDTO productDTO = ProductDTO.builder()
                    .id(product.getId())
                    .name(product.getName())
                    .brand(product.getBrand().getName())
                    .specification(product.getSpecification())
                    .image(product.getImage())
                    .is_active(product.is_active())
                    .warranty(product.getWarranty())
                    .productItems(product.getProductItems())
                    .build();
            productDTOs.add(productDTO);
        }
        model.addAttribute("products", productDTOs);

        return "user-home";
    }

    @GetMapping("/product/{productName}/{itemId}")
    public String showProduct(@PathVariable String productName,
                              @PathVariable String itemId,
                              Principal principal,
                              Model model) {
        User user = userService.findByUsername(principal.getName());
        Product product = productService.findbyName(productName);
        ProductItem productItem = productItemService.findById(Long.valueOf(itemId));

        List<Map<String, String>> specList = new ArrayList<>();

        String[] specs = product.getSpecification().split("\\n");
        for (String spec : specs) {
            String[] keyValue = spec.split(": ");
            if (keyValue.length == 2) {
                Map<String, String> specMap = new HashMap<>();
                specMap.put("key", keyValue[0].trim());
                specMap.put("value", keyValue[1].trim());
                specList.add(specMap);
            }
        }
        model.addAttribute("currentUser", user.getId());
        model.addAttribute("product", product);
        model.addAttribute("itemId", itemId);
        model.addAttribute("productItem", productItem);
        model.addAttribute("specList", specList);
        return "user-single-product";
    }

    @GetMapping("/contact")
    public String showContact(Model model) {
        return "user-contact";
    }

    @GetMapping("/info")
    public String showInfo(Model model, Principal principal) {
        if (principal == null) {
            return "redirect:/login";
        }
        User user = userService.findByUsername(principal.getName());

        model.addAttribute("user", user);
        return "user-info";
    }

    @PostMapping("/edit-info")
    public String editInfo(Principal principal,
                           @RequestParam("phone") String phone,
                           @RequestParam("email") String email,
                           @RequestParam("address") String address) {
        if (principal == null) {
            return "redirect:/login";
        }
        User user = userService.findByUsername(principal.getName());
        user.setPhone(phone);
        user.setEmail(email);
        user.setAddress(address);
        userRepository.save(user);
        return "redirect:/info";
    }

    @GetMapping("/products")
    public String showProducts(Model model) {
        List<Product> products = productService.getAllProducts();
        List<Brand> brands = brandService.getAllBrands();

        model.addAttribute("products", products);
        model.addAttribute("brands", brands);

        return "user-products";
    }

}
