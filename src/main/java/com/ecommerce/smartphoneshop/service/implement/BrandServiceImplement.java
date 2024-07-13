package com.ecommerce.smartphoneshop.service.implement;

import com.ecommerce.smartphoneshop.domain.Brand;
import com.ecommerce.smartphoneshop.dto.BrandDTO;
import com.ecommerce.smartphoneshop.repository.BrandRepository;
import com.ecommerce.smartphoneshop.service.BrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BrandServiceImplement implements BrandService {
    private final BrandRepository brandRepository;

    @Autowired
    public BrandServiceImplement(BrandRepository brandRepository) {
        this.brandRepository = brandRepository;
    }

    @Override
    public Brand saveBrand(BrandDTO brandDTO) {
        Brand brand = Brand.builder()
                            .id(brandDTO.getId())
                            .name(brandDTO.getName())
                            .build();
        return brandRepository.save(brand);
    }

    @Override
    public List<Brand> getAllBrands() {
        return brandRepository.findAll();
    }

    @Override
    public Brand updateBrand(BrandDTO brandDTO) {
        Brand brand = brandRepository.getReferenceById(String.valueOf(brandDTO.getId()));
        brand.setName(brandDTO.getName());
        return brandRepository.save(brand);
    }

    @Override
    public Brand findbyName(String brandName) {
        return brandRepository.findByName(brandName);
    }

    @Override
    public BrandDTO getbyId(String id) {
        Brand brand = brandRepository.getReferenceById(id);
        return BrandDTO.builder()
                        .id(brand.getId())
                        .name(brand.getName())
                        .build();
    }

    @Override
    public void deleteBrand(String id) {
        brandRepository.deleteById(id);
    }
}
