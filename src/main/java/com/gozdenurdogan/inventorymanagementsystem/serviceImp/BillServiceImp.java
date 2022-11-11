package com.gozdenurdogan.inventorymanagementsystem.serviceImp;

import com.gozdenurdogan.inventorymanagementsystem.model.entity.BillEntity;
import com.gozdenurdogan.inventorymanagementsystem.repository.BillRepository;
import com.gozdenurdogan.inventorymanagementsystem.service.BillService;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class BillServiceImp implements BillService {
    @Autowired
    private BillRepository billRepository;

    @Override
    public List<BillEntity> getAllBills() {
        return billRepository.findAll();
    }

    @Override
    public void saveBill(BillEntity bill) {
        this.billRepository.save(bill);
    }

    @Override
    public BillEntity getBillById(long id) {
        Optional<BillEntity> optional = billRepository.findById(id);
        BillEntity bill = null;
        if (optional.isPresent()) {
            bill = optional.get();
        } else {
            throw new RuntimeException("Bill not found for id :: " + id);
        }
        return bill;
    }

    @Override
    public void deleteBillById(long id) {
        this.billRepository.deleteById(id);
    }
}
