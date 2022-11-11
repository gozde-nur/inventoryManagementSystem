package com.gozdenurdogan.inventorymanagementsystem.serviceImp;

import com.gozdenurdogan.inventorymanagementsystem.model.entity.SoldProductEntity;
import com.gozdenurdogan.inventorymanagementsystem.repository.SoldProductRepository;
import com.gozdenurdogan.inventorymanagementsystem.service.SoldProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SoldProductServiceImp implements SoldProductService {

    @Autowired
    private SoldProductRepository soldProductRepository;

    @Override
    public List<SoldProductEntity> getAllSoldProducts() {
        return soldProductRepository.findAll();
    }

    @Override
    public void saveSoldProduct(SoldProductEntity soldProduct) {
        this.soldProductRepository.save(soldProduct);
    }

    @Override
    public SoldProductEntity getProductById(long id) {
        Optional<SoldProductEntity> optional = soldProductRepository.findById(id);
        SoldProductEntity soldProduct = null;
        if (optional.isPresent()) {
            soldProduct = optional.get();
        } else {
            throw new RuntimeException("SoldProduct not found for id :: " + id);
        }
        return soldProduct;
    }

    @Override
    public void deleteSoldProductById(long id) {
        this.soldProductRepository.deleteById(id);
    }

    @Override
    public void deleteSoldProductAllById(List<Long> soldProductIds) {
        this.soldProductRepository.deleteAllById(soldProductIds);
    }

    @Override
    public void deleteSoldProductBeObject(SoldProductEntity soldProduct) {
        this.soldProductRepository.delete(soldProduct);
    }

    public void deleteWithRawSqlQuery(long sp_id) {
        soldProductRepository.deleteWithRawSqlQuery(sp_id);
    }
}
