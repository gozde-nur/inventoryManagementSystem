package com.gozdenurdogan.inventorymanagementsystem.model.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "bills")
public class BillEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "customer_name")
    private String customer_name;

    @Column(name = "customer_surname")
    private String customer_surname;

    @Column(name = "total_price")
    private int total_price;

    @Column(name = "bill_date")
    @Temporal(TemporalType.DATE)
    private Date bill_date;

    @Column(name = "admin_confirmed")
    private Boolean admin_confirmed;

    @Column(name = "employee_confirmed")
    private Boolean employee_confirmed;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "bill_id", referencedColumnName = "id")
    List<SoldProductEntity> sold_products = new ArrayList<>();

    public BillEntity() {

    }

    public BillEntity(String customer_name, String customer_surname,
                      int total_price, Date bill_date, Boolean admin_confirmed,
                      Boolean employee_confirmed, List<SoldProductEntity> sold_products) {
        this.customer_name = customer_name;
        this.customer_surname = customer_surname;
        this.total_price = total_price;
        this.bill_date = bill_date;
        this.admin_confirmed = admin_confirmed;
        this.employee_confirmed = employee_confirmed;
        this.sold_products = sold_products;
    }
}
