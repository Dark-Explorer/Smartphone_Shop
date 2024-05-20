package com.ecommerce.smartphoneshop.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.w3c.dom.stylesheets.LinkStyle;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "shop_order")
@Builder
@Entity
public class ShopOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDateTime date_created;
    private LocalDateTime date_updated;
    private String address;
    private Long total;
    @Enumerated(EnumType.STRING)
    private OrderStatus status;
    @Enumerated(EnumType.STRING)
    private PaymentMethod payment_method;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "shopOrder")
    private List<OrderLine> orderLines;
}
