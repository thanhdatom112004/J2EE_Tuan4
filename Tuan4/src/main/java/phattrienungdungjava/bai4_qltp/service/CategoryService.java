package phattrienungdungjava.bai4_qltp.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import phattrienungdungjava.bai4_qltp.model.Category;

@Service
public class CategoryService {

	private final List<Category> categories = new ArrayList<>();

	@PostConstruct
	public void init() {
		categories.add(new Category(1, "Điện thoại"));
		categories.add(new Category(2, "Laptop"));
	}

	public List<Category> getAll() {
		return List.copyOf(categories);
	}

	public Category get(int id) {
		return categories.stream().filter(c -> c.getId() == id).findFirst().orElse(null);
	}
}
