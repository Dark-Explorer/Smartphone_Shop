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
    void deleteProduct(String id);
    void enableProduct(String id);
    void disableProduct(String id);
}
