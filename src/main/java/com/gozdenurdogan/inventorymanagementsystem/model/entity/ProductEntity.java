package com.gozdenurdogan.inventorymanagementsystem.model.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "products")
public class ProductEntity {

    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    private long id;

    @Column(name = "name")
    private String name;

    @Column(name = "price")
    private int price;

    @Column(name = "stock")
    private int stock;

    @OneToMany(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    @JoinColumn(name = "product_id",referencedColumnName = "id")
    List<SoldProductEntity> sold_products = new ArrayList<>();


    public ProductEntity() {

    }

    public ProductEntity(String name, int price, int stock) {
        this.name = name;
        this.price = price;
        this.stock = stock;
    }
}
