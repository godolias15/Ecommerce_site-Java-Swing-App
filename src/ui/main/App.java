package main;

import model.Admin;
import model.Product;
import model.StandardProduct;
import model.Electronics;
import model.Clothing;
import model.User;
import util.DatabaseManager;
import ui.MainFrame;

import javax.swing.SwingUtilities;
import java.util.List;

public class App {
    public static void main(String[] args) {
        List<Product> products = DatabaseManager.loadProducts();
        if (products.isEmpty()) {
            generateProducts(products);
            DatabaseManager.saveProducts(products);
        }

        List<User> users = DatabaseManager.loadUsers();
        if (users.isEmpty()) {
            users.add(new Admin("U001", "admin", "admin123", "admin@store.com", "SuperAdmin"));
            users.add(new User("U002", "customer", "password", "customer@store.com"));
            DatabaseManager.saveUsers(users);
        }

        SwingUtilities.invokeLater(() -> {
            MainFrame mainFrame = new MainFrame(null);
            mainFrame.setVisible(true);
        });
    }

    private static void generateProducts(List<Product> products) {
        products.add(new Clothing("P000", "Essence Mascara Lash Princess", "The Essence Mascara Lash Princess is a popular mascara known for its volumizing and lengthening effects. Achieve dramatic lashes with this long-lasting and cruelty-free formula.", 9.99, 99, "images/real_prod_0.jpg"));
        products.add(new Clothing("P001", "Eyeshadow Palette with Mirror", "The Eyeshadow Palette with Mirror offers a versatile range of eyeshadow shades for creating stunning eye looks. With a built-in mirror, it's convenient for on-the-go makeup application.", 19.99, 34, "images/real_prod_1.jpg"));
        products.add(new StandardProduct("P002", "Powder Canister", "The Powder Canister is a finely milled setting powder designed to set makeup and control shine. With a lightweight and translucent formula, it provides a smooth and matte finish.", 14.99, 89, "images/real_prod_2.jpg"));
        products.add(new StandardProduct("P003", "Red Lipstick", "The Red Lipstick is a classic and bold choice for adding a pop of color to your lips. With a creamy and pigmented formula, it provides a vibrant and long-lasting finish.", 12.99, 91, "images/real_prod_3.jpg"));
        products.add(new StandardProduct("P004", "Red Nail Polish", "The Red Nail Polish offers a rich and glossy red hue for vibrant and polished nails. With a quick-drying formula, it provides a salon-quality finish at home.", 8.99, 79, "images/real_prod_4.jpg"));
        products.add(new StandardProduct("P005", "Calvin Klein CK One", "CK One by Calvin Klein is a classic unisex fragrance, known for its fresh and clean scent. It's a versatile fragrance suitable for everyday wear.", 49.99, 29, "images/real_prod_5.jpg"));
        products.add(new StandardProduct("P006", "Chanel Coco Noir Eau De", "Coco Noir by Chanel is an elegant and mysterious fragrance, featuring notes of grapefruit, rose, and sandalwood. Perfect for evening occasions.", 129.99, 58, "images/real_prod_6.jpg"));
        products.add(new StandardProduct("P007", "Dior J'adore", "J'adore by Dior is a luxurious and floral fragrance, known for its blend of ylang-ylang, rose, and jasmine. It embodies femininity and sophistication.", 89.99, 98, "images/real_prod_7.jpg"));
        products.add(new StandardProduct("P008", "Dolce Shine Eau de", "Dolce Shine by Dolce & Gabbana is a vibrant and fruity fragrance, featuring notes of mango, jasmine, and blonde woods. It's a joyful and youthful scent.", 69.99, 4, "images/real_prod_8.jpg"));
        products.add(new StandardProduct("P009", "Gucci Bloom Eau de", "Gucci Bloom by Gucci is a floral and captivating fragrance, with notes of tuberose, jasmine, and Rangoon creeper. It's a modern and romantic scent.", 79.99, 91, "images/real_prod_9.jpg"));
        products.add(new StandardProduct("P010", "Annibale Colombo Bed", "The Annibale Colombo Bed is a luxurious and elegant bed frame, crafted with high-quality materials for a comfortable and stylish bedroom.", 1899.99, 88, "images/real_prod_10.jpg"));
        products.add(new StandardProduct("P011", "Annibale Colombo Sofa", "The Annibale Colombo Sofa is a sophisticated and comfortable seating option, featuring exquisite design and premium upholstery for your living room.", 2499.99, 60, "images/real_prod_11.jpg"));
        products.add(new StandardProduct("P012", "Bedside Table African Cherry", "The Bedside Table in African Cherry is a stylish and functional addition to your bedroom, providing convenient storage space and a touch of elegance.", 299.99, 64, "images/real_prod_12.jpg"));
        products.add(new Electronics("P013", "Knoll Saarinen Executive Conference Chair", "The Knoll Saarinen Executive Conference Chair is a modern and ergonomic chair, perfect for your office or conference room with its timeless design.", 499.99, 26, "images/real_prod_13.jpg"));
        products.add(new StandardProduct("P014", "Wooden Bathroom Sink With Mirror", "The Wooden Bathroom Sink with Mirror is a unique and stylish addition to your bathroom, featuring a wooden sink countertop and a matching mirror.", 799.99, 7, "images/real_prod_14.jpg"));
        products.add(new StandardProduct("P015", "Apple", "Fresh and crisp apples, perfect for snacking or incorporating into various recipes.", 1.99, 8, "images/real_prod_15.jpg"));
        products.add(new StandardProduct("P016", "Beef Steak", "High-quality beef steak, great for grilling or cooking to your preferred level of doneness.", 12.99, 86, "images/real_prod_16.jpg"));
        products.add(new StandardProduct("P017", "Cat Food", "Nutritious cat food formulated to meet the dietary needs of your feline friend.", 8.99, 46, "images/real_prod_17.jpg"));
        products.add(new StandardProduct("P018", "Chicken Meat", "Fresh and tender chicken meat, suitable for various culinary preparations.", 9.99, 97, "images/real_prod_18.jpg"));
        products.add(new StandardProduct("P019", "Cooking Oil", "Versatile cooking oil suitable for frying, sautéing, and various culinary applications.", 4.99, 10, "images/real_prod_19.jpg"));
        products.add(new StandardProduct("P020", "Cucumber", "Crisp and hydrating cucumbers, ideal for salads, snacks, or as a refreshing side.", 1.49, 84, "images/real_prod_20.jpg"));
        products.add(new StandardProduct("P021", "Dog Food", "Specially formulated dog food designed to provide essential nutrients for your canine companion.", 10.99, 71, "images/real_prod_21.jpg"));
        products.add(new StandardProduct("P022", "Eggs", "Fresh eggs, a versatile ingredient for baking, cooking, or breakfast.", 2.99, 9, "images/real_prod_22.jpg"));
        products.add(new StandardProduct("P023", "Fish Steak", "Quality fish steak, suitable for grilling, baking, or pan-searing.", 14.99, 74, "images/real_prod_23.jpg"));
        products.add(new StandardProduct("P024", "Green Bell Pepper", "Fresh and vibrant green bell pepper, perfect for adding color and flavor to your dishes.", 1.29, 33, "images/real_prod_24.jpg"));
        products.add(new StandardProduct("P025", "Green Chili Pepper", "Spicy green chili pepper, ideal for adding heat to your favorite recipes.", 0.99, 3, "images/real_prod_25.jpg"));
        products.add(new StandardProduct("P026", "Honey Jar", "Pure and natural honey in a convenient jar, perfect for sweetening beverages or drizzling over food.", 6.99, 34, "images/real_prod_26.jpg"));
        products.add(new StandardProduct("P027", "Ice Cream", "Creamy and delicious ice cream, available in various flavors for a delightful treat.", 5.49, 27, "images/real_prod_27.jpg"));
        products.add(new StandardProduct("P028", "Juice", "Refreshing fruit juice, packed with vitamins and great for staying hydrated.", 3.99, 50, "images/real_prod_28.jpg"));
        products.add(new StandardProduct("P029", "Kiwi", "Nutrient-rich kiwi, perfect for snacking or adding a tropical twist to your dishes.", 2.49, 99, "images/real_prod_29.jpg"));
        products.add(new StandardProduct("P030", "Lemon", "Zesty and tangy lemons, versatile for cooking, baking, or making refreshing beverages.", 0.79, 31, "images/real_prod_30.jpg"));
        products.add(new StandardProduct("P031", "Milk", "Fresh and nutritious milk, a staple for various recipes and daily consumption.", 3.49, 27, "images/real_prod_31.jpg"));
        products.add(new StandardProduct("P032", "Mulberry", "Sweet and juicy mulberries, perfect for snacking or adding to desserts and cereals.", 4.99, 99, "images/real_prod_32.jpg"));
        products.add(new StandardProduct("P033", "Nescafe Coffee", "Quality coffee from Nescafe, available in various blends for a rich and satisfying cup.", 7.99, 57, "images/real_prod_33.jpg"));
        products.add(new StandardProduct("P034", "Potatoes", "Versatile and starchy potatoes, great for roasting, mashing, or as a side dish.", 2.29, 13, "images/real_prod_34.jpg"));
        products.add(new StandardProduct("P035", "Protein Powder", "Nutrient-packed protein powder, ideal for supplementing your diet with essential proteins.", 19.99, 80, "images/real_prod_35.jpg"));
        products.add(new StandardProduct("P036", "Red Onions", "Flavorful and aromatic red onions, perfect for adding depth to your savory dishes.", 1.99, 82, "images/real_prod_36.jpg"));
        products.add(new StandardProduct("P037", "Rice", "High-quality rice, a staple for various cuisines and a versatile base for many dishes.", 5.99, 59, "images/real_prod_37.jpg"));
        products.add(new StandardProduct("P038", "Soft Drinks", "Assorted soft drinks in various flavors, perfect for refreshing beverages.", 1.99, 53, "images/real_prod_38.jpg"));
        products.add(new StandardProduct("P039", "Strawberry", "Sweet and succulent strawberries, great for snacking, desserts, or blending into smoothies.", 3.99, 46, "images/real_prod_39.jpg"));
        products.add(new StandardProduct("P040", "Tissue Paper Box", "Convenient tissue paper box for everyday use, providing soft and absorbent tissues.", 2.49, 86, "images/real_prod_40.jpg"));
        products.add(new StandardProduct("P041", "Water", "Pure and refreshing bottled water, essential for staying hydrated throughout the day.", 0.99, 53, "images/real_prod_41.jpg"));
        products.add(new StandardProduct("P042", "Decoration Swing", "The Decoration Swing is a charming addition to your home decor. Crafted with intricate details, it adds a touch of elegance and whimsy to any room.", 59.99, 47, "images/real_prod_42.jpg"));
        products.add(new StandardProduct("P043", "Family Tree Photo Frame", "The Family Tree Photo Frame is a sentimental and stylish way to display your cherished family memories. With multiple photo slots, it tells the story of your loved ones.", 29.99, 77, "images/real_prod_43.jpg"));
        products.add(new StandardProduct("P044", "House Showpiece Plant", "The House Showpiece Plant is an artificial plant that brings a touch of nature to your home without the need for maintenance. It adds greenery and style to any space.", 39.99, 28, "images/real_prod_44.jpg"));
        products.add(new StandardProduct("P045", "Plant Pot", "The Plant Pot is a stylish container for your favorite plants. With a sleek design, it complements your indoor or outdoor garden, adding a modern touch to your plant display.", 14.99, 59, "images/real_prod_45.jpg"));
        products.add(new Electronics("P046", "Table Lamp", "The Table Lamp is a functional and decorative lighting solution for your living space. With a modern design, it provides both ambient and task lighting, enhancing the atmosphere.", 49.99, 9, "images/real_prod_46.jpg"));
        products.add(new StandardProduct("P047", "Bamboo Spatula", "The Bamboo Spatula is a versatile kitchen tool made from eco-friendly bamboo. Ideal for flipping, stirring, and serving various dishes.", 7.99, 37, "images/real_prod_47.jpg"));
        products.add(new Electronics("P048", "Black Aluminium Cup", "The Black Aluminium Cup is a stylish and durable cup suitable for both hot and cold beverages. Its sleek black design adds a modern touch to your drinkware collection.", 5.99, 75, "images/real_prod_48.jpg"));
        products.add(new StandardProduct("P049", "Black Whisk", "The Black Whisk is a kitchen essential for whisking and beating ingredients. Its ergonomic handle and sleek design make it a practical and stylish tool.", 9.99, 73, "images/real_prod_49.jpg"));
    }
}
