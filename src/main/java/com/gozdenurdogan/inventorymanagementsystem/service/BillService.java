package com.gozdenurdogan.inventorymanagementsystem.service;

import com.gozdenurdogan.inventorymanagementsystem.model.entity.BillEntity;
import java.util.List;


public interface BillService {
    List<BillEntity> getAllBills();
    void saveBill(BillEntity bill);
    BillEntity getBillById(long id);
    void deleteBillById(long id);
}
