package com.gozdenurdogan.inventorymanagementsystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.gozdenurdogan.inventorymanagementsystem.model.entity.BillEntity;
import org.springframework.stereotype.Repository;

@Repository
public interface BillRepository extends JpaRepository<BillEntity, Long>{
}
