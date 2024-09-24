package service;

import entities.Product;
import entities.Category;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Warehouse {
    private final List<Product> products = new ArrayList<>();

    public void addProduct(Product product) {
        products.add(product);
    }

    public void modifyProduct(String id, String name, Category category, int rating) {
        boolean found = products.stream()
                .filter(product -> product.id().equals(id))
                .findFirst()
                .map(product -> {
                    Product updatedProduct = new Product(id, name, category, rating, product.createdDate(), LocalDate.now());
                    products.set(products.indexOf(product), updatedProduct);
                    return true;
                })
                .orElse(false);
        if (!found) {
            throw new IllegalArgumentException("Product with id " + id + " not found");
        }
    }

    public List<Product> getAllProducts() {
        return List.copyOf(products);
    }

    public Optional<Product> getProductById(String id) {
        return products.stream().filter(p -> p.id().equals(id)).findFirst();
    }

    public List<Product> getProductsByCategory(Category category) {
        return List.copyOf(
                products.stream()
                        .filter(p -> p.category().equals(category))
                        .sorted(Comparator.comparing(Product::name))
                        .toList()
        );
    }

    public List<Product> getProductsCreatedAfter(LocalDate date) {
        return List.copyOf(products.stream()
                .filter(p -> p.createdDate().isAfter(date))
                .toList()
        );
    }

    public List<Product> getProductsByModified() {
        return List.copyOf(products.stream()
                .filter(p -> !p.modifiedDate().isEqual(p.createdDate()))
                .toList()
        );
    }

    public List<Category> getCategoriesWithProducts() {
        return List.copyOf(
                products.stream()
                        .map(Product::category)
                        .distinct()
                        .toList()
        );
    }

    public Map<Category, Long> getNumberOfProductsByCategory() {
        return products.stream()
                .collect(Collectors.groupingBy(Product::category, Collectors.counting()));
    }

    public Map<Character, Long> getProductsStartingWithLetter() {
        return products.stream()
                .collect(Collectors.groupingBy(p -> p.name().charAt(0), Collectors.counting()));
    }

    public Stream<Product> getProductsByMaxRatingAndSortedByDate() {
        return products.stream()
                .filter(p -> p.rating() == 5)
                .sorted(Comparator.comparing(Product::createdDate));
    }
}


