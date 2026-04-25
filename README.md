Below is the UML class diagram for the application's model and core architecture. You can view this directly in GitHub or any Markdown viewer that supports Mermaid.js.

```mermaid
classDiagram
    %% Interface & Payment Methods
    class PaymentMethod {
        <<interface>>
        +processPayment(amount: double): boolean
        +getPaymentDetails(): String
    }
    
    class CardPayment {
        -cardNumber: String
        +processPayment(amount: double): boolean
        +getPaymentDetails(): String
    }
    
    class CashPayment {
        +processPayment(amount: double): boolean
        +getPaymentDetails(): String
    }
    
    PaymentMethod <|.. CardPayment : implements
    PaymentMethod <|.. CashPayment : implements

    %% User Hierarchy
    class User {
        -userId: String
        -username: String
        -password: String
        -email: String
        +User(userId, username, password, email)
        +getUserId(): String
        +getUsername(): String
        +getPassword(): String
        +getEmail(): String
        +setPassword(password: String): void
    }
    
    class Admin {
        -adminLevel: String
        +Admin(userId, username, password, email, adminLevel)
        +getAdminLevel(): String
        +setAdminLevel(adminLevel: String): void
    }
    
    User <|-- Admin : extends

    %% Product Hierarchy
    class Product {
        <<abstract>>
        -productId: String
        -name: String
        -description: String
        -price: double
        -stockQuantity: int
        -imagePath: String
        +Product(productId, name, description, price, stockQuantity, imagePath)
        +getDiscountRate()*: double
        +calculateFinalPrice(): double
        +getProductId(): String
        +getName(): String
        +getPrice(): double
        +getStockQuantity(): int
        +getImagePath(): String
        +setStockQuantity(stock: int): void
    }
    
    class StandardProduct {
        +StandardProduct(...)
        +getDiscountRate(): double
        +getCategory(): String
    }
    
    class Electronics {
        +Electronics(...)
        +getDiscountRate(): double
        +getCategory(): String
    }
    
    class Clothing {
        +Clothing(...)
        +getDiscountRate(): double
        +getCategory(): String
    }
    
    Product <|-- StandardProduct : extends
    Product <|-- Electronics : extends
    Product <|-- Clothing : extends

    %% UI and Utilities (simplified relationships)
    class DatabaseManager {
        <<Utility>>
        +loadProducts(): List~Product~
        +saveProducts(products: List~Product~): void
        +loadUsers(): List~User~
        +saveUsers(users: List~User~): void
    }

    class App {
        +main(args: String[]): void
    }

    class MainFrame {
        -currentUser: User
        -cart: List~Product~
    }

    App --> DatabaseManager : uses
    App --> MainFrame : initializes
    MainFrame --> Product : displays/buys
    MainFrame --> User : authenticates
    MainFrame --> PaymentMethod : uses
```
