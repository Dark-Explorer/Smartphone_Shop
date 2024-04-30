package com.ecommerce.smartphoneshop.ADMIN;

import com.ecommerce.smartphoneshop.domain.Brand;
import com.ecommerce.smartphoneshop.domain.Product;
import com.ecommerce.smartphoneshop.domain.ProductItem;
import com.ecommerce.smartphoneshop.dto.ProductDTO;
import com.ecommerce.smartphoneshop.service.BrandService;
import com.ecommerce.smartphoneshop.service.ProductItemService;
import com.ecommerce.smartphoneshop.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.*;

@Controller
@RequiredArgsConstructor
@Log
public class ProductController {
    private final ProductService productService;
    private final BrandService brandService;
    private final ProductItemService productItemService;

    @GetMapping("/admin-products")
    public String adminProducts(Model model, Principal principal) {
        if (principal == null) {
            return "redirect:/login";
        }

        List<Product> products = productService.getAllProducts();
        List<ProductDTO> productDTOs = new ArrayList<>();
        for (Product product : products) {
            ProductDTO productDTO = ProductDTO.builder()
                    .id(product.getId())
                    .name(product.getName())
                    .brand(product.getBrand().getName())
                    .specification(product.getSpecification())
                    .warranty(product.getWarranty())
                    .is_active(product.is_active())
                    .image(product.getImage())
                    .build();
            productDTOs.add(productDTO);
        }

        model.addAttribute("products", productDTOs);
        model.addAttribute("size", productDTOs.size());

        return "admin-products";
    }

    @GetMapping("add-product")
    public String addProduct(Model model) {
        List<Brand> brands = brandService.getAllBrands();
        model.addAttribute("brands", brands);
        model.addAttribute("productDTO", new ProductDTO());
        return "admin-add-product";
    }

    @PostMapping("/save-product")
    public String saveProduct(@ModelAttribute("productDTO") ProductDTO productDTO,
                              RedirectAttributes redirectAttributes,
                              @RequestParam("cpu") String cpu, @RequestParam("ram") String ram,
                              @RequestParam("rom") String rom, @RequestParam("size") String size,
                              @RequestParam("resolution") String resolution, @RequestParam("camera") String camera,
                              @RequestParam("battery") String battery, @RequestParam("charge") String charge,
                              @RequestParam("os") String os) {
//                                ){
        try {
            if (productService.findbyName(productDTO.getName()) == null){
                String specification = "CPU: " + cpu + "\n" +
                        "RAM: " + ram + "\n" +
                        "ROM: " + rom + "\n" +
                        "Kích thước màn hình: " + size + " inch\n" +
                        "Độ phân giải màn hình: " + resolution + "\n" +
                        "Camera: " + camera + "\n" +
                        "Pin: " + battery + " mAh\n" +
                        "Sạc: " + charge + " W\n" +
                        "Hệ điều hành: " + os + "\n";
                productDTO.setSpecification(specification);
                productService.saveProduct(productDTO);
                redirectAttributes.addFlashAttribute("success", "Thêm sản phẩm thành công!");
            } else redirectAttributes.addFlashAttribute("error", "Sản phẩm đã tồn tại");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Có lỗi xảy ra!");
        }
        return "redirect:/admin-products";
    }

    @GetMapping("/update-product/{id}")
    public String updateProduct(Model model, @PathVariable("id") String id) {
        ProductDTO productDTO = productService.getbyId(id);
        List<Brand> brands = brandService.getAllBrands();
        model.addAttribute("brands", brands);
        model.addAttribute("productDTO", productDTO);
        return "admin-update-product";
    }

    @PostMapping("/update-product/{id}")
    public String updateProduct(@ModelAttribute("productDTO") ProductDTO productDTO,
                                RedirectAttributes redirectAttributes,
                                @RequestParam("cpu") String cpu, @RequestParam("ram") String ram,
                                @RequestParam("rom") String rom, @RequestParam("size") String size,
                                @RequestParam("resolution") String resolution, @RequestParam("camera") String camera,
                                @RequestParam("battery") String battery, @RequestParam("charge") String charge,
                                @RequestParam("os") String os) {
        try {
            String specification = "CPU: " + cpu + "\n" +
                    "RAM: " + ram + "\n" +
                    "ROM: " + rom + "\n" +
                    "Kích thước màn hình: " + size + " inch\n" +
                    "Độ phân giải màn hình: " + resolution + "\n" +
                    "Camera: " + camera + "\n" +
                    "Pin: " + battery + " mAh\n" +
                    "Sạc: " + charge + " W\n" +
                    "Hệ điều hành: " + os + "\n";
            productDTO.setSpecification(specification);
            productService.updateProduct(productDTO);
            redirectAttributes.addFlashAttribute("success", "Cập nhật thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Có lỗi xảy ra!");
        }
        return "redirect:/admin-products";
    }

    @RequestMapping( "/delete-product")
    public String deleteProduct(@RequestParam("id") String id) {
        productService.deleteProduct(id);
        return "redirect:/admin-products";
    }

    @RequestMapping("/enable-product")
    public String enableProduct(String id, RedirectAttributes redirectAttributes) {
        try {
            productService.enableProduct(id);
            redirectAttributes.addFlashAttribute("success", "Sản phẩm sẽ hiển thị trên trang chủ!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Có lỗi xảy ra!");
        }
        return "redirect:/admin-products";
    }

    @RequestMapping("/disable-product")
    public String disableProduct(String id, RedirectAttributes redirectAttributes) {
        try {
            productService.disableProduct(id);
            redirectAttributes.addFlashAttribute("success", "Ẩn sản phẩm thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Có lỗi xảy ra!");
        }
        return "redirect:/admin-products";
    }

    @GetMapping("/products/{id}/item")
    public String productItem(@PathVariable("id") Long id, Model model, Principal principal) {
        if (principal == null) {
            return "redirect:/login";
        }

        List<ProductItem> productItems = productItemService.getItemsOfProduct(id);

        model.addAttribute("productItems", productItems);
        model.addAttribute("size", productItems.size());

        return "admin-product-item";
    }

    @GetMapping("/products/{productId}/item/{itemId}")
    public String addItem(@PathVariable("productId") Long productId, @PathVariable("itemId") Long itemId, Model model) {
        model.addAttribute("productItem", new ProductItem());
        return "admin-add-item";
    }

    @PostMapping("/products/{productId}/item/{itemId}")
    public String addItem(@PathVariable("productId") Long productId,
                          @PathVariable("itemId") Long itemId,
                          Model model, RedirectAttributes redirectAttribute,
                          @ModelAttribute("productItem") ProductItem productItem) {

        

        return "redirect:/products/{id}/item";
    }
}