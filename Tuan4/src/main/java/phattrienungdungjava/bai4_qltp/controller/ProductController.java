package phattrienungdungjava.bai4_qltp.controller;

import java.io.IOException;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.Valid;
import phattrienungdungjava.bai4_qltp.model.Category;
import phattrienungdungjava.bai4_qltp.model.Product;
import phattrienungdungjava.bai4_qltp.service.CategoryService;
import phattrienungdungjava.bai4_qltp.service.ProductService;

@Controller
@RequestMapping("/products")
public class ProductController {

	private final ProductService productService;
	private final CategoryService categoryService;

	public ProductController(ProductService productService, CategoryService categoryService) {
		this.productService = productService;
		this.categoryService = categoryService;
	}

	@GetMapping("")
	public String index(Model model) {
		model.addAttribute("listproduct", productService.getAll());
		return "product/products";
	}

	@GetMapping("/create")
	public String createGet(Model model) {
		Product product = new Product();
		Category defaultCat = new Category();
		defaultCat.setId(1);
		product.setCategory(defaultCat);
		model.addAttribute("product", product);
		model.addAttribute("categories", categoryService.getAll());
		return "product/create";
	}

	@PostMapping("/create")
	public String createPost(@Valid @ModelAttribute("product") Product newProduct, BindingResult result,
			@RequestParam("imageProduct") MultipartFile imageProduct, Model model) {
		if (result.hasErrors()) {
			model.addAttribute("categories", categoryService.getAll());
			return "product/create";
		}
		try {
			productService.updateImage(newProduct, imageProduct);
		} catch (IllegalArgumentException ex) {
			model.addAttribute("uploadError", ex.getMessage());
			model.addAttribute("categories", categoryService.getAll());
			return "product/create";
		} catch (IOException ex) {
			model.addAttribute("uploadError", "Không thể lưu tệp hình ảnh.");
			model.addAttribute("categories", categoryService.getAll());
			return "product/create";
		}
		Category category = resolveCategory(newProduct);
		if (category == null) {
			model.addAttribute("uploadError", "Danh mục không hợp lệ.");
			model.addAttribute("categories", categoryService.getAll());
			return "product/create";
		}
		newProduct.setCategory(category);
		productService.add(newProduct);
		return "redirect:/products";
	}

	@GetMapping("/edit/{id}")
	public String editGet(@PathVariable int id, Model model) {
		Product product = productService.get(id);
		if (product == null) {
			return "error/404";
		}
		model.addAttribute("product", product);
		model.addAttribute("categories", categoryService.getAll());
		return "product/edit";
	}

	@PostMapping("/edit")
	public String editPost(@Valid @ModelAttribute("product") Product editProduct, BindingResult result,
			@RequestParam("imageProduct") MultipartFile imageProduct, Model model) {
		if (result.hasErrors()) {
			model.addAttribute("categories", categoryService.getAll());
			return "product/edit";
		}
		Product existing = productService.get(editProduct.getId());
		if (existing == null) {
			return "error/404";
		}
		try {
			if (imageProduct != null && !imageProduct.isEmpty()) {
				productService.updateImage(editProduct, imageProduct);
			} else {
				editProduct.setImage(existing.getImage());
			}
		} catch (IllegalArgumentException ex) {
			model.addAttribute("uploadError", ex.getMessage());
			model.addAttribute("categories", categoryService.getAll());
			return "product/edit";
		} catch (IOException ex) {
			model.addAttribute("uploadError", "Không thể lưu tệp hình ảnh.");
			model.addAttribute("categories", categoryService.getAll());
			return "product/edit";
		}
		Category category = resolveCategory(editProduct);
		if (category == null) {
			model.addAttribute("uploadError", "Danh mục không hợp lệ.");
			model.addAttribute("categories", categoryService.getAll());
			return "product/edit";
		}
		editProduct.setCategory(category);
		productService.update(editProduct);
		return "redirect:/products";
	}

	@PostMapping("/delete/{id}")
	public String delete(@PathVariable int id) {
		productService.delete(id);
		return "redirect:/products";
	}

	private Category resolveCategory(Product product) {
		if (product.getCategory() == null) {
			return null;
		}
		return categoryService.get(product.getCategory().getId());
	}
}
