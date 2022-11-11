package com.gozdenurdogan.inventorymanagementsystem.service;

import com.gozdenurdogan.inventorymanagementsystem.model.entity.ProductEntity;

import java.util.List;

public interface ProductService {
    List<ProductEntity> getAllProducts();
    void saveProduct(ProductEntity product);
    ProductEntity getProductById(long id);
    void deleteProductById(long id);
}
