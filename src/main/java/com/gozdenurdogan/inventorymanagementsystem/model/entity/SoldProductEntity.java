package com.gozdenurdogan.inventorymanagementsystem.model.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "sold_products")
public class SoldProductEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "piece")
    private int piece;

    @Column(name = "in_basket")
    private Boolean in_basket;

    @Column(name = "product_id")
    private long product_id;

    @Column(name = "bill_id")
    private long bill_id;

    public SoldProductEntity() {

    }
}
