package com.ecommerce.smartphoneshop.service.implement;

import com.ecommerce.smartphoneshop.domain.Brand;
import com.ecommerce.smartphoneshop.domain.Product;
import com.ecommerce.smartphoneshop.dto.ProductDTO;
import com.ecommerce.smartphoneshop.repository.BrandRepository;
import com.ecommerce.smartphoneshop.repository.ProductRepository;
import com.ecommerce.smartphoneshop.service.ProductService;
import com.ecommerce.smartphoneshop.utils.ImageUpload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;
import java.util.List;

@Service
public class ProductServiceImplement implements ProductService {
    private final ProductRepository productRepository;
    private final BrandRepository brandRepository;
    private final ImageUpload imageUpload;

    @Autowired
    public ProductServiceImplement(ProductRepository productRepository, BrandRepository brandRepository, ImageUpload imageUpload) {
        this.productRepository = productRepository;
        this.brandRepository = brandRepository;
        this.imageUpload = imageUpload;
    }

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public Product saveProduct(ProductDTO productDTO) throws IOException {
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
        Product product = productRepository.getReferenceById(Math.toIntExact(productDTO.getId()));

        product.setName(productDTO.getName());
        product.setSpecification(productDTO.getSpecification());
        product.setWarranty(productDTO.getWarranty());
        product.setBrand(product.getBrand());
        product.set_active(product.is_active());

        return productRepository.save(product);
    }

    @Override
    public Product findbyName(String productName) {
        return productRepository.findByName(productName);
    }

    @Override
    public ProductDTO getbyId(String id) {
        Product product = productRepository.getReferenceById(Math.toIntExact(Integer.parseInt(id)));
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
    public void deleteProduct(String id) {
        productRepository.deleteById(Integer.valueOf(id));
    }

    @Override
    public void enableProduct(String id) {
        Product product = productRepository.getReferenceById(Math.toIntExact(Integer.parseInt(id)));
        product.set_active(true);
        productRepository.save(product);
    }

    @Override
    public void disableProduct(String id) {
        Product product = productRepository.getReferenceById(Math.toIntExact(Integer.parseInt(id)));
        product.set_active(false);
        productRepository.save(product);
    }
}
