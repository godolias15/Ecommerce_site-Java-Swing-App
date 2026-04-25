package model;

public class CashPayment implements PaymentMethod {
    @Override
    public boolean processPayment(double amount) {
        System.out.println("Processing cash on delivery payment of $" + amount);
        return true; // Simulate success
    }

    @Override
    public String getPaymentDetails() {
        return "Cash on Delivery";
    }
}
