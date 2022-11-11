package com.gozdenurdogan.inventorymanagementsystem.serviceImp;

import com.gozdenurdogan.inventorymanagementsystem.model.entity.ProductEntity;
import com.gozdenurdogan.inventorymanagementsystem.repository.ProductRepository;
import com.gozdenurdogan.inventorymanagementsystem.service.ProductService;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class ProductServiceImp implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Override
    public List<ProductEntity> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public void saveProduct(ProductEntity product) {
        this.productRepository.save(product);
    }

    @Override
    public ProductEntity getProductById(long id) {
        Optional<ProductEntity> optional = productRepository.findById(id);
        ProductEntity product = null;
        if (optional.isPresent()) {
            product = optional.get();
        } else {
            throw new RuntimeException(" Product not found for id :: " + id);
        }
        return product;
    }

    @Override
    public void deleteProductById(long id) {
        this.productRepository.deleteById(id);
    }
}
