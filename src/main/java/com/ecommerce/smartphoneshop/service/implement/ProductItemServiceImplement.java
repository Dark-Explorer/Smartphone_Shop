package com.ecommerce.smartphoneshop.service.implement;

import com.ecommerce.smartphoneshop.domain.Product;
import com.ecommerce.smartphoneshop.domain.ProductItem;
import com.ecommerce.smartphoneshop.repository.ProductItemRepository;
import com.ecommerce.smartphoneshop.service.ProductItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductItemServiceImplement implements ProductItemService {
    private final ProductItemRepository productItemRepository;

    @Autowired
    public ProductItemServiceImplement(ProductItemRepository productItemRepository) {
        this.productItemRepository = productItemRepository;
    }


    @Override
    public List<ProductItem> getItemsOfProduct(Long id) {
        return productItemRepository.findItemsOfProduct(id);
    }

    @Override
    public ProductItem saveProductItem(ProductItem productItem) {
        return productItemRepository.save(productItem);
    }

    @Override
    public ProductItem updateProductItem(ProductItem productItem) {
        return productItemRepository.save(productItem);
    }

    @Override
    public void deleteProductItem(Long id) {
        productItemRepository.deleteById(id);
    }
}
