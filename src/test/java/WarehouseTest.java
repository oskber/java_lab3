import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.Warehouse;
import entities.Product;
import entities.Category;

import java.time.LocalDate;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class WarehouseTest {
    private Warehouse warehouse;
    private Product product;
    private Category category;

    @BeforeEach
    void setUp() {
        warehouse = new Warehouse();
        category = Category.ELECTRONICS;
        product = new Product("1", "Laptop", category, 5, LocalDate.now(), LocalDate.now());
    }

    @Test
    void givenProductWhenAddProductRuns() {
        warehouse.addProduct(product);
        assertThat(warehouse.getAllProducts()).contains(product);
    }

    @Test
    void givenProductWithEmptyNameWhenAddProductRuns() {
        assertThrows(IllegalArgumentException.class, () -> warehouse.addProduct(new Product("1", "", category, 5, LocalDate.now(), LocalDate.now())));
    }

    @Test
    void givenProductWithNullNameWhenAddProductRuns() {
        assertThrows(IllegalArgumentException.class, () -> warehouse.addProduct(new Product("1", null, category, 5, LocalDate.now(), LocalDate.now())));
    }

    @Test
    void givenProductWithNullDateWhenAddProductRuns() {
        assertThrows(IllegalArgumentException.class, () -> warehouse.addProduct(new Product("1", "phone", category, 5, null, LocalDate.now())));
    }

    @Test
    void givenProductWithNullModifiedDateWhenAddProductRuns() {
        Product product = new Product("1", "phone", category, 5, LocalDate.now(), null);
        assertThat(product.modifiedDate()).isEqualTo(product.createdDate());
    }

    @Test
    void givenModifiedProductWhenModifyProductRuns() {
        warehouse.addProduct(product);
        warehouse.modifyProduct("1", "Laptop", category, 4);
        Product modifiedProduct = new Product("1", "Laptop", category, 4, LocalDate.now(), LocalDate.now());
        assertThat(warehouse.getAllProducts()).contains(modifiedProduct);
    }

    @Test
    void givenAllProductsWhenGetAllProductsRuns() {
        warehouse.addProduct(product);
        warehouse.addProduct(new Product("2", "Smartphone", category, 4, LocalDate.now(), LocalDate.now()));
        assertThat(warehouse.getAllProducts()).contains(product);
    }

    @Test
    void givenProductByIdWhenGetProductByIdRuns() {
        warehouse.addProduct(product);
        assertThat(warehouse.getProductById("1")).isPresent().contains(product);
    }

    @Test
    void givenNonExistentProductIdWhenGetProductByIdRuns() {
        warehouse.addProduct(product);
        assertThat(warehouse.getProductById("non-existent-id")).isNotPresent();
    }

    @Test
    void givenProductsByCategoryWhenGetProductsByCategoryRuns() {
        warehouse.addProduct(product);
        warehouse.addProduct(new Product("2", "Smartphone", category, 4, LocalDate.now(), LocalDate.now()));
        assertThat(warehouse.getProductsByCategory(category)).contains(product);
    }

    @Test
    void givenProductsCreatedAfterWhenGetProductsCreatedAfterRuns() {
        warehouse.addProduct(product);
        warehouse.addProduct(new Product("2", "Smartphone", category, 4, LocalDate.now().minusDays(1), LocalDate.now()));
        assertThat(warehouse.getProductsCreatedAfter(LocalDate.now().minusDays(1))).containsExactly(product);
    }

    @Test
    void givenProductsModifiedWhenGetProductsByModifiedRuns() {
        warehouse.addProduct(product);
        warehouse.addProduct(new Product("2", "Smartphone", category, 4, LocalDate.now().minusDays(1), LocalDate.now().minusDays(1)));
        warehouse.modifyProduct("2", "Smartphone", category, 3);
        Product modifiedProduct = new Product("2", "Smartphone", category, 3, LocalDate.now().minusDays(1), LocalDate.now());
        assertThat(warehouse.getProductsByModified()).contains(modifiedProduct);
    }

    @Test
    void givenNonExistentProductIdWhenModifyProductRuns() {
        warehouse.addProduct(product);
        assertThrows(IllegalArgumentException.class, () -> warehouse.modifyProduct("non-existent-id", "Smartphone", category, 4));
    }

    @Test
    void givenAllCategoriesWithAProductWhenGetCategoriesWithProductsRuns() {
        warehouse.addProduct(product);
        warehouse.addProduct(new Product("2", "T-shirt", Category.CLOTHING, 5, LocalDate.now(), LocalDate.now()));
        warehouse.addProduct(new Product("3", "Car", Category.TOYS, 4, LocalDate.now(), LocalDate.now()));
        assertThat(warehouse.getCategoriesWithProducts()).contains(category);
    }

    @Test
    void givenHowManyProductsInCategoryWhenGetNumberOfProductsByCategoryRuns() {
        warehouse.addProduct(product);
        warehouse.addProduct(new Product("2", "Smartphone", category, 4, LocalDate.now(), LocalDate.now()));
        warehouse.addProduct(new Product("3", "Burger", Category.FOOD, 3, LocalDate.now(), LocalDate.now()));
        Map<Category, Long> result = warehouse.getNumberOfProductsByCategory();
        assertThat(result).hasSize(2);
        assertThat(result.get(category)).isEqualTo(2L);
    }

    @Test
    void givenMapOfProductsStartingWithLetterWhenGetProductsStartingWithLetterRuns() {
        warehouse.addProduct(product);
        warehouse.addProduct(new Product("2", "Smartphone", category, 4, LocalDate.now(), LocalDate.now()));
        warehouse.addProduct(new Product("3", "Burger", Category.FOOD, 3, LocalDate.now(), LocalDate.now()));
        warehouse.addProduct(new Product("4", "Shoes", Category.CLOTHING, 5, LocalDate.now(), LocalDate.now()));
        Map<Character, Long> result = warehouse.getProductsStartingWithLetter();
        assertThat(result).hasSize(3);
        assertThat(result.get('L')).isEqualTo(1L);
        assertThat(result.get('S')).isEqualTo(2L);
        assertThat(result.get('B')).isEqualTo(1L);
    }

    @Test
    void givenProductWithMaxRatingAndSortedByDateWhenGetProductsByMaxRatingAndSortedByDateRuns() {
        warehouse.addProduct(product);
        warehouse.addProduct(new Product("2", "Smartphone", category, 4, LocalDate.now(), LocalDate.now()));
        warehouse.addProduct(new Product("3", "Burger", Category.FOOD, 3, LocalDate.now(), LocalDate.now()));
        warehouse.addProduct(new Product("4", "Shoes", Category.CLOTHING, 5, LocalDate.now(), LocalDate.now()));
        assertThat(warehouse.getProductsByMaxRatingAndSortedByDate()).containsExactly(product, new Product("4", "Shoes", Category.CLOTHING, 5, LocalDate.now(), LocalDate.now()));
    }
}