package phattrienungdungjava.bai4_qltp.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import phattrienungdungjava.bai4_qltp.model.Product;

@Service
public class ProductService {

	private final List<Product> listProduct = new ArrayList<>();

	public List<Product> getAll() {
		return List.copyOf(listProduct);
	}

	public List<Product> search(String query) {
		if (query == null || query.isBlank()) {
			return getAll();
		}
		String q = query.trim().toLowerCase(Locale.ROOT);
		return listProduct.stream()
				.filter(p -> p.getName() != null && p.getName().toLowerCase(Locale.ROOT).contains(q))
				.toList();
	}

	public Product get(int id) {
		return listProduct.stream().filter(p -> p.getId() == id).findFirst().orElse(null);
	}

	public void add(Product newProduct) {
		int nextId = listProduct.stream().map(Product::getId).max(Comparator.naturalOrder()).orElse(0) + 1;
		newProduct.setId(nextId);
		listProduct.add(newProduct);
	}

	public void update(Product editProduct) {
		Product existing = get(editProduct.getId());
		if (existing == null) {
			return;
		}
		existing.setName(editProduct.getName());
		existing.setPrice(editProduct.getPrice());
		existing.setImage(editProduct.getImage());
		existing.setCategory(editProduct.getCategory());
	}

	public void delete(int id) {
		listProduct.removeIf(p -> p.getId() == id);
	}

	public void updateImage(Product newProduct, MultipartFile imageProduct) throws IOException {
		if (imageProduct == null || imageProduct.isEmpty()) {
			return;
		}
		String contentType = imageProduct.getContentType();
		if (contentType == null || !contentType.startsWith("image/")) {
			throw new IllegalArgumentException("Tệp tải lên không phải là hình ảnh!");
		}
		Path dir = Path.of("static", "images").toAbsolutePath().normalize();
		Files.createDirectories(dir);
		String original = Objects.requireNonNullElse(imageProduct.getOriginalFilename(), "image");
		String filename = UUID.randomUUID() + "_" + original;
		Path target = dir.resolve(filename);
		Files.copy(imageProduct.getInputStream(), target, StandardCopyOption.REPLACE_EXISTING);
		newProduct.setImage(filename);
	}
}
