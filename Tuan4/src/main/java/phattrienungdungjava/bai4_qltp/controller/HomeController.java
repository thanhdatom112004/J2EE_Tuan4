package phattrienungdungjava.bai4_qltp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import phattrienungdungjava.bai4_qltp.service.ProductService;

@Controller
public class HomeController {

	private final ProductService productService;

	public HomeController(ProductService productService) {
		this.productService = productService;
	}

	@GetMapping("/home/search")
	public String search(@RequestParam(value = "q", required = false) String q, Model model) {
		model.addAttribute("listproduct", productService.search(q));
		return "product/products";
	}
}
