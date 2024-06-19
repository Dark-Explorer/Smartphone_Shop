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
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
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
    public String adminProducts(Model model, Principal principal,
                                @Param("keyword") String keyword,
                                @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo) {
        if (principal == null) {
            return "redirect:/login";
        }

//        List<Product> products = productService.getAllProducts();
        Page<Product> products = productService.getAllProducts(pageNo);
        if (keyword != null && !keyword.isEmpty()) {
            products = productService.searchProduct(keyword, pageNo);
            model.addAttribute("keyword", keyword);
        }

//        List<ProductDTO> productDTOs = new ArrayList<>();
//        for (Product product : products) {
//            ProductDTO productDTO = ProductDTO.builder()
//                    .id(product.getId())
//                    .name(product.getName())
//                    .brand(product.getBrand().getName())
//                    .specification(product.getSpecification())
//                    .warranty(product.getWarranty())
//                    .is_active(product.is_active())
//                    .image(product.getImage())
//                    .build();
//            productDTOs.add(productDTO);
//        }

//        model.addAttribute("products", productDTOs);
//        model.addAttribute("size", productDTOs.size());
        model.addAttribute("products", products);
        model.addAttribute("totalPage", products.getTotalPages());
        model.addAttribute("currentPage", pageNo);

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
                              @RequestParam("os") String os, @RequestParam("image") String image) {
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
                productDTO.setImage(image);
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

        String[] specs = {"CPU", "RAM", "ROM", "size", "resolution", "camera", "battery", "charge", "os"};
        String[] lines = productDTO.getSpecification().split("\\n");

        int i = 0;
        for (String line : lines) {
            String[] parts = line.split(": ");
            String value = parts[1].trim();
            if (i == 3 || i == 6 || i == 7) {
                String[] split = value.split(" ");
                value = split[0].trim();
                double numVal = Double.parseDouble(value);
                model.addAttribute(specs[i++], numVal);
            } else model.addAttribute(specs[i++], value);
        }

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
                                @RequestParam("os") String os, @RequestParam("image") String image) {
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
            productDTO.setImage(image);
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

    // Show items of a product
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

    @GetMapping("/products/{productId}/item/add")
    public String addItem(@PathVariable("productId") Long productId, Model model) {
        Product product = productService.findById(productId);
        model.addAttribute("product", product);
        model.addAttribute("productItem", new ProductItem());
        return "admin-add-item";
    }

    @PostMapping("/save-item/{id}")
    public String addItem(@ModelAttribute("productItem") ProductItem productItem,
                          @ModelAttribute("product") Product product,
                          @RequestParam("sku") String sku,
                          @RequestParam("variation") String variation,
                          @RequestParam("qty") int qtyInStock,
                          @RequestParam("image") String image,
                          @RequestParam("price") Long price,
                          RedirectAttributes redirectAttribute) {
        try {
            if (productItemService.findByName(productItem.getVariation(), product.getId()) == null) {
                productItemService.saveProductItem(sku, variation, qtyInStock, image, price, product);
                redirectAttribute.addFlashAttribute("success", "Thêm sản phẩm thành công!");
            } else redirectAttribute.addFlashAttribute("error", "Loại sản phẩm đã tồn tại");
        } catch (Exception e) {
            redirectAttribute.addFlashAttribute("error", "Có lỗi xảy ra!");
        }

        return "redirect:/admin-products";
    }

    @GetMapping("/products/{productId}/item/{itemId}")
    public String updateItem(@PathVariable("productId") Long productId,
                             @PathVariable("itemId") Long itemId,
                             Model model) {
        Product product = productService.findById(productId);
        ProductItem productItem = productItemService.findById(itemId);
        model.addAttribute("product", product);
        model.addAttribute("productItem", productItem);
        return "admin-update-item";
    }

    @PostMapping("/update-item/{productId}/{itemId}")
    public String updateItem(@PathVariable("productId") Long productId,
                             @PathVariable("itemId") Long productItemId,
                             @RequestParam("sku") String sku,
                             @RequestParam("variation") String variation,
                             @RequestParam("qty") int qtyInStock,
                             @RequestParam("image") String image,
                             @RequestParam("price") Long price,
                             RedirectAttributes redirectAttributes) {
        try {
            Product product = productService.findById(productId);
            ProductItem productItem = productItemService.findById(productItemId);
            productItemService.updateProductItem(product, productItem, sku, variation, qtyInStock, image, price);
            redirectAttributes.addFlashAttribute("success", "Cập nhật loại sản phẩm thành công!");
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Có lỗi xảy ra");
        }
        return "redirect:/admin-products";
    }

    @RequestMapping("/delete-item")
    public String deleteItem(@RequestParam("id") Long id) {
        productItemService.deleteProductItem(id);
        return "redirect:/admin-products";
    }
}