package com.gozdenurdogan.inventorymanagementsystem.web;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import com.gozdenurdogan.inventorymanagementsystem.model.entity.BillEntity;
import com.gozdenurdogan.inventorymanagementsystem.model.entity.ProductEntity;
import com.gozdenurdogan.inventorymanagementsystem.model.entity.SoldProductEntity;
import com.gozdenurdogan.inventorymanagementsystem.service.BillService;
import com.gozdenurdogan.inventorymanagementsystem.service.ProductService;
import com.gozdenurdogan.inventorymanagementsystem.service.SoldProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/employee")
public class EmployeeController {

	@Autowired
	private ProductService productService;

	@Autowired
	private SoldProductService soldProductService;

	@Autowired
	private BillService billService;

	@GetMapping("/listEmployeeProducts")
	public String listEmployeeProducts(Model theModel, HttpServletResponse response) {
		
		List<SoldProductEntity> soldProductss = soldProductService.getAllSoldProducts();
		List<SoldProductEntity> soldProducts = new ArrayList<SoldProductEntity>();
		for(SoldProductEntity sp:soldProductss) {
			if(sp.getIn_basket() == true) {
				soldProducts.add(sp);
			}
		}
		int urunkalem = 0;
		int totalfiyat = 0;
		for(SoldProductEntity soldProduct : soldProducts) {
			urunkalem += soldProduct.getPiece();
			totalfiyat += productService.getProductById(soldProduct.getProduct_id()).getPrice() * soldProduct.getPiece();
		}
		Cookie cookieUrunKalem = new Cookie("urunkalem", String.valueOf(urunkalem));
		Cookie cookieTotalFiyat = new Cookie("totalfiyat",String.valueOf(totalfiyat));
		response.addCookie(cookieUrunKalem);
		response.addCookie(cookieTotalFiyat);
		
		
		
		List<ProductEntity> theProducts = productService.getAllProducts();
		theModel.addAttribute("products", theProducts);
		return "listEmployeeProducts";
	}

	@GetMapping("/seeProduct/{id}")
	public String seeProduct(@PathVariable(value = "id") long id, Model model) {
		ProductEntity product = productService.getProductById(id);
		model.addAttribute("product", product);
		SoldProductEntity soldProduct = new SoldProductEntity();
		model.addAttribute("soldProduct", soldProduct);
		return "seeProduct";
	}

	@PostMapping("/addBasket")
	public String addBasket(String piece, String productId, HttpServletResponse response) {
		List<BillEntity> bills = billService.getAllBills();
		Boolean haveEmptyBill = false;
		long emptyBillId = 0;
		// Hiç fatura yoksa
		if (bills.size() == 0) {
			BillEntity bill = new BillEntity();
			bill.setCustomer_name("-");
			bill.setCustomer_surname("-");
			bill.setTotal_price(0);
			bill.setBill_date(java.sql.Date.valueOf(LocalDate.now()));
			bill.setEmployee_confirmed(haveEmptyBill);
			bill.setAdmin_confirmed(false);
			billService.saveBill(bill);
			addBasket(piece, productId, response);
		} else {
			// hazırda fatura var ama şu anda kullanılacak boş fatura yok
			for (BillEntity bill : bills) {
				if (bill.getEmployee_confirmed() == false) {
					haveEmptyBill = true;
					emptyBillId = bill.getId();
				}
			}

			///// --------
			Boolean alreadyInBasketItem = false;
			List<SoldProductEntity> soldProductss = soldProductService.getAllSoldProducts();
			for (SoldProductEntity sp : soldProductss) {
				if (sp.getIn_basket() == true) {
					alreadyInBasketItem = true;
					break;
				}
			}

			if (alreadyInBasketItem) {
				ProductEntity product = productService.getProductById(Long.parseLong(productId));
				product.setStock(product.getStock() - Integer.parseInt(piece));
				productService.saveProduct(product);
			} else {
				ProductEntity product = productService.getProductById(Long.parseLong(productId));
				product.setStock(product.getStock() - (Integer.parseInt(piece) / 2));
				productService.saveProduct(product);
			}
			///// -------

			// sold product ekleme kısmı
			if (haveEmptyBill) {
				Boolean alreadyExist = true;
				List<SoldProductEntity> soldProducts = soldProductService.getAllSoldProducts();
				for (SoldProductEntity soldProduct : soldProducts) {
					if (soldProduct.getProduct_id() == Long.parseLong(productId)
							&& soldProduct.getIn_basket() == true) {
						soldProduct.setPiece(soldProduct.getPiece() + Integer.parseInt(piece));
						soldProductService.saveSoldProduct(soldProduct);
						alreadyExist = false;
					}
				}
				if (alreadyExist) {
					SoldProductEntity soldProduct = new SoldProductEntity();
					soldProduct.setProduct_id(Long.parseLong(productId));
					soldProduct.setPiece(Integer.parseInt(piece));
					soldProduct.setIn_basket(true);
					soldProduct.setBill_id(emptyBillId);
					soldProductService.saveSoldProduct(soldProduct);
				}

			} else {
				BillEntity bill = new BillEntity();
				bill.setCustomer_name("-");
				bill.setCustomer_surname("-");
				bill.setTotal_price(0);
				bill.setBill_date(java.sql.Date.valueOf(LocalDate.now()));
				bill.setEmployee_confirmed(false);
				bill.setAdmin_confirmed(false);
				billService.saveBill(bill);
				addBasket(piece, productId, response);
			}

			/////
		}

	
		
//		List<SoldProduct> soldProducts = billService.getBillById(emptyBillId).getSold_products();
//		int urunkalem = 0;
//		int totalfiyat = 0;
//		for(SoldProduct soldProduct : soldProducts) {
//			urunkalem += soldProduct.getPiece();
//			totalfiyat += productService.getProductById(soldProduct.getProduct_id()).getPrice() * soldProduct.getPiece();
//		}
//		Cookie cookieUrunKalem = new Cookie("urunkalem", String.valueOf(urunkalem));
//		Cookie cookieTotalFiyat = new Cookie("totalfiyat",String.valueOf(totalfiyat));
//		response.addCookie(cookieUrunKalem);
//		response.addCookie(cookieTotalFiyat);
		
		
		return "redirect:/employee/listEmployeeProducts";
	}

	@GetMapping("/seeBasket")
	public String seeBasket(Model theModel) {
		List<SoldProductEntity> allSoldProducts = soldProductService.getAllSoldProducts();
		List<SoldProductEntity> soldProducts = new ArrayList<SoldProductEntity>();
		for (SoldProductEntity sp : allSoldProducts) {
			if (sp.getIn_basket() == true) {
				soldProducts.add(sp);
			}
		}
		List<ProductEntity> products = productService.getAllProducts();
		theModel.addAttribute("products", products);
		theModel.addAttribute("soldProducts", soldProducts);
		return "seeBasket";
	}

	@GetMapping("/createBill")
	public String createBill(Model theModel) {
		List<BillEntity> bills = billService.getAllBills();
		BillEntity modelBill = null;
		for (BillEntity bill : bills) {
			if (bill.getEmployee_confirmed() == false) {
				modelBill = bill;
			}
		}
		int totalPrice = 0;
		List<SoldProductEntity> soldProducts = soldProductService.getAllSoldProducts();
		for (SoldProductEntity soldProduct : soldProducts) {
			if (soldProduct.getIn_basket()) {
				totalPrice += productService.getProductById(soldProduct.getProduct_id()).getPrice()
						* soldProduct.getPiece();
			}
		}
		System.out.println(totalPrice + " ** *** * *");
		modelBill.setTotal_price(totalPrice);
		theModel.addAttribute("modelBill", modelBill);
		return "createBill";
	}

	@PostMapping("/saveBill")
	public String saveBill(@ModelAttribute("modelBill") BillEntity bill,HttpServletResponse response) {
		List<BillEntity> bills = billService.getAllBills();
		BillEntity nowBill = null;
		for (BillEntity b : bills) {
			if (b.getEmployee_confirmed() == false) {
				nowBill = b;
				break;
			}
		}
		nowBill.setAdmin_confirmed(false);
		nowBill.setEmployee_confirmed(true);
		nowBill.setBill_date(bill.getBill_date());
		nowBill.setCustomer_name(bill.getCustomer_name());
		nowBill.setCustomer_surname(bill.getCustomer_surname());
		nowBill.setTotal_price(bill.getTotal_price());
		nowBill.setBill_date(java.sql.Date.valueOf(LocalDate.now()));
		billService.saveBill(nowBill);

		List<SoldProductEntity> soldProducts = soldProductService.getAllSoldProducts();
		for (SoldProductEntity sp : soldProducts) {
			if (sp.getIn_basket() == true) {
				sp.setIn_basket(false);
				soldProductService.saveSoldProduct(sp);
			}
		}
		
		Cookie cookieUrunKalem = new Cookie("urunkalem", "0");
		Cookie cookieTotalFiyat = new Cookie("totalfiyat","0");
		response.addCookie(cookieUrunKalem);
		response.addCookie(cookieTotalFiyat);
		
		return "redirect:/employee/home";
	}

	@GetMapping("/cancelTheSale")
	public String cancelTheSale(HttpServletResponse response) {
		List<SoldProductEntity> soldProducts = soldProductService.getAllSoldProducts();
		// List<Long> soldProductIds = new ArrayList<Long>();
		List<SoldProductEntity> selectSoldProducts = new ArrayList<SoldProductEntity>();
		long billId = 0;
		for (SoldProductEntity soldProduct : soldProducts) {
			if (soldProduct.getIn_basket() == true) {
				selectSoldProducts.add(soldProduct);
				soldProductService.deleteSoldProductById(soldProduct.getId());
				billId = soldProduct.getBill_id();
				System.out.println(billId + "fdsa");
			}
		}

		billService.deleteBillById(billId);

		for (SoldProductEntity selectSoldProduct : selectSoldProducts) {
			ProductEntity product = productService.getProductById(selectSoldProduct.getProduct_id());
			product.setStock(product.getStock() + selectSoldProduct.getPiece());
			productService.saveProduct(product);
		}

		
		Cookie cookieUrunKalem = new Cookie("urunkalem", "0");
		Cookie cookieTotalFiyat = new Cookie("totalfiyat","0");
		response.addCookie(cookieUrunKalem);
		response.addCookie(cookieTotalFiyat);
		
		return "redirect:/employee/home";
	}
	
	

}