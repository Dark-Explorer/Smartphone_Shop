package com.ecommerce.smartphoneshop.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "product_item")
public class ProductItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String SKU;
    private String variation;
    private int qty_in_stock;
    private String image;
    private Long price;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "product_id")
    private Product product;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "productItem")
    private List<ShoppingCartItem> shoppingCartItems;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "productItem")
    private List<OrderLine> orderLines;
}
