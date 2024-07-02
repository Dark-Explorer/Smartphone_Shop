package com.ecommerce.smartphoneshop.service;

import com.ecommerce.smartphoneshop.domain.Product;
import com.ecommerce.smartphoneshop.dto.ProductDTO;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public interface ProductService {
    List<Product> getAllProducts();
    Page<Product> getAllProducts(int pageNo);
    Product saveProduct(ProductDTO productDTO) throws IOException;
    Product updateProduct(ProductDTO productDTO);
    Product findbyName(String productName);
    ProductDTO getbyId(String id);
    Product findById(Long id);
    void deleteProduct(String id);
    void enableProduct(String id);
    void disableProduct(String id);
    List<Product> filterProduct(String brandName, Long min, Long max);
    Page<Product> searchProduct(String keyword, Integer pageNo);
    Page<Product> filterProduct(String brandName, Long min, Long max, Integer pageNo);
}
