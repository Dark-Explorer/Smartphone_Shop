package com.ecommerce.smartphoneshop.CUSTOMER;

import com.ecommerce.smartphoneshop.domain.Product;
import com.ecommerce.smartphoneshop.domain.ProductItem;
import com.ecommerce.smartphoneshop.domain.User;
import com.ecommerce.smartphoneshop.dto.ProductDTO;
import com.ecommerce.smartphoneshop.service.ProductItemService;
import com.ecommerce.smartphoneshop.service.ProductService;
import com.ecommerce.smartphoneshop.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

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

//    @GetMapping("/{name}/{id}")
//    public String showProduct(@PathVariable String name, @PathVariable String id, Model model) {
//        Product product = productService.findbyName(name);
//        ProductItem productItem = productItemService.findById(Long.valueOf(id));
//        model.addAttribute("product", product);
//        model.addAttribute("productItem", productItem);
//        return "user-single-product";
//    }

    @GetMapping("/contact")
    public String showContact(Model model) {
        return "user-contact";
    }

}
