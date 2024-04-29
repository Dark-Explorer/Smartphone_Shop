package com.ecommerce.smartphoneshop.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name = "product")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String specification;
    @Lob
    @Column(columnDefinition = "MEDIUMBLOB")
    private String image;
    private int warranty;
    private boolean is_active = true;

    @ManyToOne
    @JoinColumn(name = "brand_id")
    private Brand brand;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "product")
    private List<ProductItem> productItems;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "product")
    private List<Variation> variations;
}
