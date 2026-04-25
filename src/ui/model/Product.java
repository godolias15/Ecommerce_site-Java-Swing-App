package model;

import java.io.Serializable;

public abstract class Product implements Serializable {
    private static final long serialVersionUID = 2L;

    private String productId;
    private String name;
    private String description;
    private double price;
    private int stockQuantity;
    private String imagePath; // NEW: Image path field

    public Product(String productId, String name, String description, double price, int stockQuantity, String imagePath) {
        this.productId = productId;
        this.name = name;
        this.description = description;
        this.price = price;
        this.stockQuantity = stockQuantity;
        this.imagePath = imagePath;
    }

    // --- OOP REQUIREMENTS: POLYMORPHIC METHODS ---
    public abstract double getDiscountRate();
    public abstract String getCategory();

    public double getFinalPrice() {
        return price * (1.0 - getDiscountRate());
    }
    // ---------------------------------------------

    public String getProductId() { return productId; }
    public void setProductId(String productId) { this.productId = productId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public int getStockQuantity() { return stockQuantity; }
    public void setStockQuantity(int stockQuantity) { this.stockQuantity = stockQuantity; }

    public String getImagePath() { return imagePath; }
    public void setImagePath(String imagePath) { this.imagePath = imagePath; }
}
