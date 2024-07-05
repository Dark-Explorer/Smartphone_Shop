package com.ecommerce.smartphoneshop.service.implement;

import com.ecommerce.smartphoneshop.domain.OrderLine;
import com.ecommerce.smartphoneshop.domain.Product;
import com.ecommerce.smartphoneshop.domain.ProductItem;
import com.ecommerce.smartphoneshop.repository.OrderLineRepository;
import com.ecommerce.smartphoneshop.repository.ProductItemRepository;
import com.ecommerce.smartphoneshop.service.ProductItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductItemServiceImplement implements ProductItemService {
    private final ProductItemRepository productItemRepository;
    private final OrderLineRepository orderLineRepository;

    @Autowired
    public ProductItemServiceImplement(ProductItemRepository productItemRepository, OrderLineRepository orderLineRepository) {
        this.productItemRepository = productItemRepository;
        this.orderLineRepository = orderLineRepository;
    }

    @Override
    public List<ProductItem> getItemsOfProduct(Long id) {
        return productItemRepository.findItemsOfProduct(id);
    }

    @Override
    public ProductItem saveProductItem(String sku, String variation, int qtyInStock, String image, Long price, Product product) {
        ProductItem productItem = ProductItem.builder()
                .SKU(sku)
                .variation(variation)
                .qty_in_stock(qtyInStock)
                .product(product)
                .image(image)
                .price(price)
                .build();
        return productItemRepository.save(productItem);
    }

    @Override
    public ProductItem updateProductItem(Product product, ProductItem productItem, String sku, String variation, int qtyInStock, String image, Long price) {
        productItem.setSKU(sku);
        productItem.setVariation(variation);
        productItem.setQty_in_stock(qtyInStock);
        productItem.setImage(image);
        productItem.setPrice(price);
        productItem.setProduct(product);

        return productItemRepository.save(productItem);
    }

    @Override
    public void deleteProductItem(Long id) {
        ProductItem productItem = productItemRepository.findById(id).orElse(null);
        if (productItem != null) {
            for (OrderLine orderLine : productItem.getOrderLines()) {
                orderLine.setProductItem(null);
                orderLineRepository.save(orderLine);
            }
        }
        productItemRepository.deleteById(id);
    }

    @Override
    public ProductItem findByName(String name, Long productId) {
        return productItemRepository.findByName(name, productId);
    }

    @Override
    public ProductItem findById(Long id) {
        return productItemRepository.findById(id).orElse(null);
    }
}
