package com.ecommerce.smartphoneshop.service;

import com.ecommerce.smartphoneshop.domain.Product;
import com.ecommerce.smartphoneshop.dto.ProductDTO;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public interface ProductService {
    List<Product> getAllProducts();
    Product saveProduct(ProductDTO productDTO) throws IOException;
    Product updateProduct(ProductDTO productDTO);
    Product findbyName(String productName);
    ProductDTO getbyId(String id);
    Product findById(Long id);
    void deleteProduct(String id);
    void enableProduct(String id);
    void disableProduct(String id);
    List<Product> sortByPrice(List<Product> products);
    List<Product> filterByBrand(String brand);
    List<Product> filterByPrice(Long min, Long max);
    List<Product> filterProduct(String brandName, Long min, Long max);
}
