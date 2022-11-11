package com.gozdenurdogan.inventorymanagementsystem.repository;

import com.gozdenurdogan.inventorymanagementsystem.model.entity.SoldProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;

public interface SoldProductRepository extends JpaRepository<SoldProductEntity,Long> {
    @Modifying
    @Transactional
    @Query(value = "DELETE FROM sold_products where id =: sp_id)", nativeQuery = true)
    void deleteWithRawSqlQuery(@Param("sp_id") Long sp_id);
}
