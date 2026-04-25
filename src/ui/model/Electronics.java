package model;

public class Electronics extends Product {
    private static final long serialVersionUID = 1L;

    public Electronics(String id, String name, String desc, double price, int stock, String img) {
        super(id, name, desc, price, stock, img);
    }

    @Override
    public double getDiscountRate() {
        return 0.10; // 10% discount for electronics
    }

    @Override
    public String getCategory() {
        return "Electronics";
    }
}
