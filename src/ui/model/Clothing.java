package model;

public class Clothing extends Product {
    private static final long serialVersionUID = 1L;

    public Clothing(String id, String name, String desc, double price, int stock, String img) {
        super(id, name, desc, price, stock, img);
    }

    @Override
    public double getDiscountRate() {
        return 0.20; // 20% discount for clothing
    }

    @Override
    public String getCategory() {
        return "Clothing";
    }
}
