package com.ecommerce.smartphoneshop.ADMIN;

import com.ecommerce.smartphoneshop.domain.Brand;
import com.ecommerce.smartphoneshop.dto.BrandDTO;
import com.ecommerce.smartphoneshop.dto.ProductDTO;
import com.ecommerce.smartphoneshop.service.BrandService;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
@Log
public class BrandController {
    private final BrandService brandService;

    @GetMapping("/admin-brands")
    public String brands(Model model, Principal principal) {
        if (principal == null) {
            return "redirect:/login";
        }

        List<Brand> brands = brandService.getAllBrands();
        List<BrandDTO> brandDTOs = new ArrayList<>();
        for (Brand brand : brands) {
            BrandDTO brandDTO = BrandDTO.builder()
                            .id(brand.getId())
                            .name(brand.getName())
                            .build();
            brandDTOs.add(brandDTO);
        }

        model.addAttribute("brands", brandDTOs);
        model.addAttribute("size", brandDTOs.size());

        return "admin-brands";
    }

    @GetMapping("add-brand")
    public String addBrand(Model model) {
        model.addAttribute("brandDTO", new BrandDTO());
        return "admin-add-brand";
    }

    @PostMapping("/save-brand")
    public String saveBrand(@ModelAttribute("brandDTO") BrandDTO brandDTO,
                            RedirectAttributes redirectAttributes) {
        try {
            if (brandService.findbyName(brandDTO.getName()) == null){
                redirectAttributes.addFlashAttribute("success", "Thêm thương hiệu thành công!");
                brandService.saveBrand(brandDTO);
            } else redirectAttributes.addFlashAttribute("error", "Thương hiệu đã tồn tại");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Có lỗi xảy ra!");
        }
        return "redirect:/admin-brands";
    }

    @GetMapping("/update-brand/{id}")
    public String updateBrand(Model model, @PathVariable("id") String id) {
        BrandDTO brandDTO = brandService.getbyId(id);
        model.addAttribute("brandDTO", brandDTO);
        return "admin-update-brand";
    }

    @PostMapping("/update-brand/{id}")
    public String updateBrand(@ModelAttribute("brandDTO") BrandDTO brandDTO,
                              RedirectAttributes redirectAttributes) {
        try {
            brandService.updateBrand(brandDTO);
            redirectAttributes.addFlashAttribute("success", "Cập nhật thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Có lỗi xảy ra!");
        }
        return "redirect:/admin-brands";
    }

    @RequestMapping( "/delete-brand")
    public String deleteBrand(@RequestParam("id") String id) {
        brandService.deleteBrand(id);
        return "redirect:/admin-brands";
    }
}
