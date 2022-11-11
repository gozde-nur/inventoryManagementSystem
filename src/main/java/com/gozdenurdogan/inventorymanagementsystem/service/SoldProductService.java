package com.gozdenurdogan.inventorymanagementsystem.service;

import com.gozdenurdogan.inventorymanagementsystem.model.entity.SoldProductEntity;

import java.util.List;

public interface SoldProductService {

    List<SoldProductEntity> getAllSoldProducts();
    void saveSoldProduct(SoldProductEntity soldProduct);
    SoldProductEntity getProductById(long id);
    void deleteSoldProductById(long id);
    void deleteSoldProductAllById(List<Long> soldProductIds);
    void deleteSoldProductBeObject(SoldProductEntity soldProduct);
    void deleteWithRawSqlQuery(long sp_id);
}
