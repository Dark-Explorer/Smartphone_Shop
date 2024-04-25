package com.ecommerce.smartphoneshop.service;

import com.ecommerce.smartphoneshop.domain.Brand;
import com.ecommerce.smartphoneshop.dto.BrandDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface BrandService {
    List<Brand> getAllBrands();
    Brand saveBrand(BrandDTO brandDTO);
    Brand updateBrand(BrandDTO brandDTO);
    Brand findbyName(String brandName);
    BrandDTO getbyId(String id);
    void deleteBrand(String id);
}
