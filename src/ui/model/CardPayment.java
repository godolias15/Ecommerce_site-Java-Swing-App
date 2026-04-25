package model;

public class CardPayment implements PaymentMethod {
    private String cardNumber;

    public CardPayment(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    @Override
    public boolean processPayment(double amount) {
        // Simulate contacting bank API
        System.out.println("Processing credit card payment of $" + amount + " for card " + cardNumber);
        return true; // Simulate success
    }

    @Override
    public String getPaymentDetails() {
        String last4 = cardNumber.length() >= 4 ? cardNumber.substring(cardNumber.length() - 4) : cardNumber;
        return "Credit Card (ending in " + last4 + ")";
    }
}
