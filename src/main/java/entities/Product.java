package entities;

import java.time.LocalDate;

public record Product(String id, String name, Category category, int rating, LocalDate createdDate, LocalDate modifiedDate) {
    public Product {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Product name cannot be null or empty");
        }
        if (createdDate == null) {
            throw new IllegalArgumentException("Created date cannot be null");
        }
        if (modifiedDate == null) {
            modifiedDate = createdDate;
        }
    }
}
