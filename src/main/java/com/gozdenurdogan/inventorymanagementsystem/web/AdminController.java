package com.gozdenurdogan.inventorymanagementsystem.web;

import com.gozdenurdogan.inventorymanagementsystem.model.entity.BillEntity;
import com.gozdenurdogan.inventorymanagementsystem.model.entity.ProductEntity;
import com.gozdenurdogan.inventorymanagementsystem.model.entity.SoldProductEntity;
import com.gozdenurdogan.inventorymanagementsystem.service.BillService;
import com.gozdenurdogan.inventorymanagementsystem.service.ProductService;
import com.gozdenurdogan.inventorymanagementsystem.service.SoldProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private ProductService productService;

    @Autowired
    private SoldProductService soldProductService;

    @Autowired
    private BillService billService;

    @GetMapping("/listAdminProducts")
    public String listProducts(Model theModel) {
        List<ProductEntity> theProducts = productService.getAllProducts();
        theModel.addAttribute("products", theProducts);
        return "listAdminProducts";
    }


    @GetMapping("/showNewProductForm")
    public String showNewProductForm(Model model) {
        ProductEntity product = new ProductEntity();
        model.addAttribute("product", product);
        return "newProduct";
    }

    @PostMapping("/saveProduct")
    public String saveProduct(@ModelAttribute("product") ProductEntity product) {
        productService.saveProduct(product);
        return "redirect:/admin/listAdminProducts";
    }

    @GetMapping("/showUpdateProductForm/{id}")
    public String showUpdateProductForm(@PathVariable( value = "id") long id, Model model) {
        ProductEntity product = productService.getProductById(id);
        model.addAttribute("product", product);
        return "updateProduct";
    }


    @GetMapping("/deleteProduct/{id}")
    public String deleteEmployee(@PathVariable (value = "id") long id) {
        this.productService.deleteProductById(id);
        return "redirect:/admin/listAdminProducts";
    }

    @GetMapping("/unconfirmedBills")
    public String unconfirmedBills(Model theModel) {
        List<BillEntity> bills = billService.getAllBills();
        List<BillEntity> unconfirmedBills = new ArrayList<BillEntity>();
        for(BillEntity bill:bills) {
            if(bill.getAdmin_confirmed() == false) {
                unconfirmedBills.add(bill);
            }
        }
        theModel.addAttribute("unconfirmedBills",unconfirmedBills);
        return "unconfirmedBills";
    }

    @GetMapping("/confirmBill/{id}")
    public String confirmBill(@PathVariable (value = "id") long id) {
        BillEntity bill = billService.getBillById(id);
        bill.setAdmin_confirmed(true);
        billService.saveBill(bill);
        return "redirect:/admin/unconfirmedBills";
    }

    @GetMapping("/billHistory")
    public String billHistory(Model theModel) {
        List<BillEntity> bills = billService.getAllBills();
        List<BillEntity> confirmedBills = new ArrayList<BillEntity>();
        for(BillEntity bill:bills) {
            if(bill.getAdmin_confirmed() == true) {
                confirmedBills.add(bill);
            }
        }
        theModel.addAttribute("confirmedBills",confirmedBills);
        return "billHistory";
    }

    @GetMapping("/cancelTheBill/{id}")
    public String cancelTheBill(@PathVariable (value = "id") long id) {
        List<SoldProductEntity> soldProducts = soldProductService.getAllSoldProducts();
        List<SoldProductEntity> selectSoldProducts = new ArrayList<SoldProductEntity>();
        for (SoldProductEntity soldProduct : soldProducts) {
            if (soldProduct.getBill_id() == id) {
                selectSoldProducts.add(soldProduct);
                soldProductService.deleteSoldProductById(soldProduct.getId());
            }
        }

        billService.deleteBillById(id);

        for (SoldProductEntity selectSoldProduct : selectSoldProducts) {
            ProductEntity product = productService.getProductById(selectSoldProduct.getProduct_id());
            product.setStock(product.getStock() + selectSoldProduct.getPiece());
            productService.saveProduct(product);
        }


        return "redirect:/admin/listAdminProducts";
    }
}
