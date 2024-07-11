package com.ecommerce.smartphoneshop.service.implement;

import com.ecommerce.smartphoneshop.domain.Product;
import com.ecommerce.smartphoneshop.dto.ProductDTO;
import com.ecommerce.smartphoneshop.repository.BrandRepository;
import com.ecommerce.smartphoneshop.repository.ProductRepository;
import com.ecommerce.smartphoneshop.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductServiceImplement implements ProductService {
    private final ProductRepository productRepository;
    private final BrandRepository brandRepository;
    private final int PAGE_SIZE = 8;

    @Autowired
    public ProductServiceImplement(ProductRepository productRepository, BrandRepository brandRepository) {
        this.productRepository = productRepository;
        this.brandRepository = brandRepository;
    }

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public Page<Product> getAllProducts(int pageNo) {
        Pageable pageable = PageRequest.of(pageNo - 1, PAGE_SIZE);
        return productRepository.findAll(pageable);
    }

    @Override
    public Product saveProduct(ProductDTO productDTO) {
        Product product = Product.builder()
                .id(productDTO.getId())
                .name(productDTO.getName())
                .brand(brandRepository.findByName(productDTO.getBrand()))
                .specification(productDTO.getSpecification())
                .warranty(productDTO.getWarranty())
                .image(productDTO.getImage())
                .is_active(true)
                .build();
        return productRepository.save(product);
    }

    @Override
    public Product updateProduct(ProductDTO productDTO) {
        Product product = productRepository.getReferenceById(productDTO.getId());

        product.setName(productDTO.getName());
        product.setSpecification(productDTO.getSpecification());
        product.setWarranty(productDTO.getWarranty());
        product.setBrand(product.getBrand());
        product.set_active(product.is_active());
        product.setImage(productDTO.getImage());

        return productRepository.save(product);
    }

    @Override
    public Product findbyName(String productName) {
        return productRepository.findByName(productName);
    }

    @Override
    public ProductDTO getbyId(String id) {
        Product product = productRepository.getReferenceById(Long.valueOf(id));
        return ProductDTO.builder()
                                .id(product.getId())
                                .name(product.getName())
                                .specification(product.getSpecification())
                                .image(product.getImage())
                                .warranty(product.getWarranty())
                                .is_active(product.is_active())
                                .build();
    }

    @Override
    public Product findById(Long id){
        return productRepository.findById(id).orElse(null);
    }

    @Override
    public void deleteProduct(String id) {
        productRepository.deleteById(Long.valueOf(id));
    }

    @Override
    public void enableProduct(String id) {
        Product product = productRepository.getReferenceById(Long.valueOf(id));
        product.set_active(true);
        productRepository.save(product);
    }

    @Override
    public void disableProduct(String id) {
        Product product = productRepository.getReferenceById(Long.valueOf(id));
        product.set_active(false);
        productRepository.save(product);
    }

    @Override
    public List<Product> filterProduct(String brandName, Long min, Long max) {
        List<Product> allProducts = productRepository.findAll();

        if (StringUtils.hasText(brandName) && min != null && max != null) {
            return allProducts.stream()
                    .filter(product -> product.getBrand().getName().equalsIgnoreCase(brandName))
                    .filter(product -> product.getProductItems().getFirst().getPrice() >= min && product.getProductItems().getFirst().getPrice() <= max)
                    .collect(Collectors.toList());
        } else if (StringUtils.hasText(brandName)) {
            return allProducts.stream()
                    .filter(product -> product.getBrand().getName().equalsIgnoreCase(brandName))
                    .collect(Collectors.toList());
        } else if (min != null && max != null) {
            return allProducts.stream()
                    .filter(product -> product.getProductItems().getFirst().getPrice() >= min && product.getProductItems().getFirst().getPrice() <= max)
                    .collect(Collectors.toList());
        } else {
            return allProducts;
        }
    }

    @Override
    public Page<Product> searchProduct(String keyword, Integer pageNo) {
        List<Product> list = productRepository.findByKeyword(keyword);
        Pageable pageable = PageRequest.of(pageNo - 1, PAGE_SIZE);

        int start = (pageNo - 1) * PAGE_SIZE;
        int end = Math.min(start + PAGE_SIZE, list.size());

        List<Product> contentPage = list.subList(start, end);

        return new PageImpl<>(contentPage, pageable, list.size());
    }

    @Override
    public Page<Product> filterProduct(String brandName, Long min, Long max, Integer pageNo) {
        List<Product> allProducts = productRepository.findByBrandName(brandName).stream()
                .filter(product -> product.getProductItems().getFirst().getPrice() >= min && product.getProductItems().getFirst().getPrice() <= max)
                .collect(Collectors.toList());
        System.out.println(allProducts.size());

        Pageable pageable = PageRequest.of(pageNo - 1, PAGE_SIZE);

        int start = (int) pageable.getOffset();
        int end = Math.min((int) pageable.getOffset() + pageable.getPageSize(), allProducts.size());

        List<Product> contentPage = allProducts.subList(start, end);

        return new PageImpl<>(contentPage, pageable, allProducts.size());
    }
}
