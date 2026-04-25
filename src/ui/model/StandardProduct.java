package model;

public class StandardProduct extends Product {
    private static final long serialVersionUID = 1L;

    public StandardProduct(String id, String name, String desc, double price, int stock, String img) {
        super(id, name, desc, price, stock, img);
    }

    @Override
    public double getDiscountRate() {
        return 0.0; // 0% discount
    }

    @Override
    public String getCategory() {
        return "Standard";
    }
}
