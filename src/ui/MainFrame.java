package ui;

import model.Product;
import model.User;
import util.DatabaseManager;
import ui.components.RoundedButton;
import ui.components.RoundedPanel;
import ui.components.ModernScrollBarUI;
import ui.components.Toast;
import model.Admin;
import ui.AdminDashboardPanel;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.io.File;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainFrame extends JFrame {
    private User currentUser;
    private List<Product> availableProducts;
    private List<Product> cart;

    private JPanel contentPanel;
    private CardLayout cardLayout;

    // Store UI
    private JPanel gridPanel;
    private JScrollPane storeScrollPane;
    private JTextField searchField;
    private String currentSortOption = "Default";

    // Cart UI
    private JPanel cartItemsPanel;
    private JLabel totalLabel;

    private JLabel userLabel;

    public MainFrame(User user) {
        this.currentUser = user;
        this.cart = new ArrayList<>();
        this.availableProducts = DatabaseManager.loadProducts();

        setTitle("MainFrame Store");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1100, 800);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(245, 247, 250));

        add(createNavbar(), BorderLayout.NORTH);

        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);

        contentPanel.add(createHomePage(), "HOME");
        contentPanel.add(createStorefront(), "STORE");
        contentPanel.add(createCartPage(), "CART");
        contentPanel.add(new AdminDashboardPanel(), "ADMIN");

        add(contentPanel, BorderLayout.CENTER);

        // Start on Home page, hide search bar initially
        cardLayout.show(contentPanel, "HOME");
        searchField.setVisible(false);
    }

    private JPanel createNavbar() {
        JPanel nav = new JPanel(new BorderLayout());
        nav.setBackground(new Color(44, 62, 80));
        nav.setBorder(new EmptyBorder(15, 30, 15, 30));

        JLabel logo = new JLabel("MainFrame Store");
        logo.setFont(new Font("Segoe UI", Font.BOLD, 22));
        logo.setForeground(Color.WHITE);

        // --- SEARCH BAR ---
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        searchPanel.setOpaque(false);
        searchField = new JTextField(30);
        searchField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        searchField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(52, 73, 94), 2),
            new EmptyBorder(6, 12, 6, 12)
        ));
        searchField.setBackground(new Color(52, 73, 94));
        searchField.setForeground(Color.WHITE);
        searchField.setCaretColor(Color.WHITE);

        // Add live search filtering
        searchField.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { filterProducts(); }
            public void removeUpdate(DocumentEvent e) { filterProducts(); }
            public void changedUpdate(DocumentEvent e) { filterProducts(); }
        });

        searchPanel.add(searchField);

        // --- LINKS ---
        JPanel navLinks = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 0));
        navLinks.setOpaque(false);

        JButton homeBtn = createNavButton("\uD83C\uDFE0 Home"); // Home icon
        homeBtn.addActionListener(e -> {
            cardLayout.show(contentPanel, "HOME");
            searchField.setVisible(false);
        });

        JButton storeBtn = createNavButton("\uD83C\uDFEA Store"); // Store icon
        storeBtn.addActionListener(e -> {
            cardLayout.show(contentPanel, "STORE");
            searchField.setVisible(true);
        });

        String usernameDisplay = (currentUser != null) ? currentUser.getUsername() : "Guest";
        userLabel = new JLabel("\uD83D\uDC64 " + usernameDisplay); // User icon
        userLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        userLabel.setForeground(new Color(189, 195, 199));

        JButton cartBtn = createNavButton("\uD83D\uDED2 Cart"); // Shopping cart
        cartBtn.addActionListener(e -> {
            if (currentUser == null) {
                LoginDialog loginDialog = new LoginDialog(this);
                loginDialog.setVisible(true);
                User loggedIn = loginDialog.getAuthenticatedUser();
                if (loggedIn != null) {
                    this.currentUser = loggedIn;
                    for (Component c : ((JPanel)((BorderLayout)((JPanel)getContentPane().getComponent(0)).getLayout()).getLayoutComponent(BorderLayout.EAST)).getComponents()) {
                        if ("adminBtn".equals(c.getName())) {
                            c.setVisible(currentUser instanceof Admin);
                        }
                    }
                    userLabel.setText("\uD83D\uDC64 " + currentUser.getUsername());
                    Toast.showSuccess(this, "Logged in as " + currentUser.getUsername());
                } else {
                    return;
                }
            }
            searchField.setVisible(false); // Hide search on cart page
            updateCartUI();
            cardLayout.show(contentPanel, "CART");
        });

        navLinks.add(homeBtn);
        navLinks.add(storeBtn);
        navLinks.add(cartBtn);
        JButton adminBtn = createNavButton("\u2699 Admin Panel");
        adminBtn.setVisible(currentUser instanceof Admin);
        adminBtn.setName("adminBtn");
        adminBtn.addActionListener(e -> {
            cardLayout.show(contentPanel, "ADMIN");
            searchField.setVisible(false);
        });
        navLinks.add(adminBtn);

        // Helper to update admin visibility dynamically
        navLinks.add(Box.createRigidArea(new Dimension(10, 0)));
        navLinks.add(userLabel);

        nav.add(logo, BorderLayout.WEST);
        nav.add(searchPanel, BorderLayout.CENTER);
        nav.add(navLinks, BorderLayout.EAST);

        return nav;
    }

    private JButton createNavButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setForeground(Color.WHITE);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private JPanel createHomePage() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(245, 247, 250));

        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setOpaque(false);

        // --- Hero Banner ---
        RoundedPanel heroPanel = new RoundedPanel();
        heroPanel.setBackground(new Color(44, 62, 80));
        heroPanel.setLayout(new BoxLayout(heroPanel, BoxLayout.Y_AXIS));
        heroPanel.setBorder(new EmptyBorder(80, 60, 80, 60));

        JLabel heroTitle = new JLabel("Experience the Future of Shopping");
        heroTitle.setFont(new Font("Segoe UI", Font.BOLD, 36));
        heroTitle.setForeground(Color.WHITE);
        heroTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel heroSub = new JLabel("Discover premium electronics, fashion, and more at unbeatable prices.");
        heroSub.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        heroSub.setForeground(new Color(189, 195, 199));
        heroSub.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton shopNowBtn = new RoundedButton("Shop Now Collection", new Color(52, 152, 219), new Color(41, 128, 185));
        shopNowBtn.setPreferredSize(new Dimension(220, 50));
        shopNowBtn.setMaximumSize(new Dimension(220, 50));
        shopNowBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        shopNowBtn.setFont(new Font("Segoe UI", Font.BOLD, 16));
        shopNowBtn.addActionListener(e -> {
            searchField.setVisible(true);
            cardLayout.show(contentPanel, "STORE");
        });

        heroPanel.add(heroTitle);
        heroPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        heroPanel.add(heroSub);
        heroPanel.add(Box.createRigidArea(new Dimension(0, 40)));
        heroPanel.add(shopNowBtn);

        JPanel heroWrapper = new JPanel(new BorderLayout());
        heroWrapper.setOpaque(false);
        heroWrapper.setBorder(new EmptyBorder(30, 40, 10, 40));
        heroWrapper.add(heroPanel, BorderLayout.CENTER);

        content.add(heroWrapper);

        // --- Featured Products Section ---
        JPanel featuredWrapper = new JPanel(new BorderLayout());
        featuredWrapper.setOpaque(false);
        featuredWrapper.setBorder(new EmptyBorder(40, 40, 40, 40));
        featuredWrapper.setMaximumSize(new Dimension(Integer.MAX_VALUE, 900));

        JLabel featuredTitle = new JLabel("Trending Products");
        featuredTitle.setFont(new Font("Segoe UI", Font.BOLD, 26));
        featuredTitle.setForeground(new Color(44, 62, 80));
        featuredTitle.setBorder(new EmptyBorder(0, 0, 20, 0));
        featuredWrapper.add(featuredTitle, BorderLayout.NORTH);

        JPanel featuredGrid = new JPanel(new GridLayout(0, 4, 20, 20));
        featuredGrid.setOpaque(false);

        // Showcase top 8 products
        int displayCount = 0;
            for (Product p : availableProducts) {
            if (displayCount >= 8) break;
            featuredGrid.add(createProductCard(p));
            displayCount++;
        }

        featuredWrapper.add(featuredGrid, BorderLayout.CENTER);
        content.add(featuredWrapper);
        content.add(Box.createVerticalGlue());

        JScrollPane scrollPane = new JScrollPane(content);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.getVerticalScrollBar().setUI(new ModernScrollBarUI());

        panel.add(scrollPane, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createStorefront() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(245, 247, 250));

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(Color.WHITE);
        topPanel.setBorder(new EmptyBorder(30, 30, 20, 30));

        JLabel heroTitle = new JLabel("All Products");
        heroTitle.setFont(new Font("Segoe UI", Font.BOLD, 28));
        heroTitle.setForeground(new Color(44, 62, 80));
        topPanel.add(heroTitle, BorderLayout.WEST);

        String[] sortOptions = {"Default", "Price: Low to High", "Price: High to Low", "Name: A to Z", "Name: Z to A"};
        JComboBox<String> sortBox = new JComboBox<>(sortOptions);
        sortBox.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        sortBox.setPreferredSize(new Dimension(200, 35));
        sortBox.addActionListener(e -> {
            currentSortOption = (String) sortBox.getSelectedItem();
            filterProducts();
        });

        JPanel sortPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        sortPanel.setOpaque(false);
        JLabel sortLabel = new JLabel("Sort By: ");
        sortLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        sortPanel.add(sortLabel);
        sortPanel.add(sortBox);

        topPanel.add(sortPanel, BorderLayout.EAST);
        panel.add(topPanel, BorderLayout.NORTH);

        // Responsive Grid Panel
        gridPanel = new JPanel(new GridLayout(0, 3, 20, 20));
        gridPanel.setBackground(new Color(245, 247, 250));
        gridPanel.setBorder(new EmptyBorder(20, 30, 30, 30));

        populateGrid(availableProducts);

        storeScrollPane = new JScrollPane(gridPanel);
        storeScrollPane.setBorder(null);
        storeScrollPane.getVerticalScrollBar().setUnitIncrement(16);
        storeScrollPane.getVerticalScrollBar().setUI(new ModernScrollBarUI()); // Custom modern scrollbar

        panel.add(storeScrollPane, BorderLayout.CENTER);
        return panel;
    }

    private void filterProducts() {
        String query = searchField.getText().toLowerCase();
        List<Product> filtered = new ArrayList<>();
            for (Product p : availableProducts) {
            if (p.getName().toLowerCase().contains(query) || p.getDescription().toLowerCase().contains(query)) {
                filtered.add(p);
            }
        }

        if ("Price: Low to High".equals(currentSortOption)) {
            filtered.sort((p1, p2) -> Double.compare(p1.getPrice(), p2.getPrice()));
        } else if ("Price: High to Low".equals(currentSortOption)) {
            filtered.sort((p1, p2) -> Double.compare(p2.getPrice(), p1.getPrice()));
        } else if ("Name: A to Z".equals(currentSortOption)) {
            filtered.sort((p1, p2) -> p1.getName().compareToIgnoreCase(p2.getName()));
        } else if ("Name: Z to A".equals(currentSortOption)) {
            filtered.sort((p1, p2) -> p2.getName().compareToIgnoreCase(p1.getName()));
        }

        populateGrid(filtered);
    }

    private void populateGrid(List<Product> products) {
        gridPanel.removeAll();
        for (Product p : products) {
            gridPanel.add(createProductCard(p));
        }
        gridPanel.revalidate();
        gridPanel.repaint();
    }

    private JPanel createProductCard(Product p) {
        RoundedPanel card = new RoundedPanel(); // Uses our new rounded panel with hover logic inside
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(Color.WHITE);
        card.setBorder(new EmptyBorder(20, 20, 20, 20));

        try {
            ImageIcon originalIcon = new ImageIcon(p.getImagePath());
            Image scaledImage = originalIcon.getImage().getScaledInstance(120, 120, Image.SCALE_SMOOTH);
            JLabel imageLabel = new JLabel(new ImageIcon(scaledImage));
            imageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            imageLabel.setBorder(new EmptyBorder(0, 0, 15, 0));
            card.add(imageLabel);
        } catch (Exception e) {}

        JLabel nameLabel = new JLabel(p.getName());
        nameLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        nameLabel.setForeground(new Color(44, 62, 80));
        nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Truncate description slightly for uniform cards
        String desc = p.getDescription();
        if (desc.length() > 60) desc = desc.substring(0, 60) + "...";
        JLabel descLabel = new JLabel("<html><div style='text-align: center; width: 160px;'>" + desc + "</div></html>");
        descLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        descLabel.setForeground(new Color(127, 140, 141));
        descLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        descLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JPanel pricePanel = new JPanel();
        pricePanel.setLayout(new BoxLayout(pricePanel, BoxLayout.Y_AXIS));
        pricePanel.setOpaque(false);
        pricePanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        double discountRate = p.getDiscountRate();
        int discount = (int)(discountRate * 100);
        double origPrice = p.getPrice();

        JLabel discountLabel = new JLabel();
        if (discount > 0) {
            discountLabel.setText(String.format("<html><div style=\"text-align: center;\"><strike>$%.2f</strike> <span style=\"color:#e74c3c; font-weight:bold;\">-%d%%</span></div></html>", origPrice, discount));
        } else {
            discountLabel.setText(" ");
        }
        discountLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        discountLabel.setForeground(new Color(127, 140, 141));
        discountLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        discountLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel priceLabel = new JLabel(String.format("$%.2f", p.getFinalPrice()));
        priceLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        priceLabel.setForeground(new Color(39, 174, 96));
        priceLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        priceLabel.setHorizontalAlignment(SwingConstants.CENTER);

        pricePanel.add(discountLabel);
        pricePanel.add(priceLabel);

        // Uses our new Animated Rounded Button
        JButton addBtn = new RoundedButton("Add to Cart", new Color(52, 152, 219), new Color(41, 128, 185));
        addBtn.setPreferredSize(new Dimension(150, 40));
        addBtn.setMaximumSize(new Dimension(150, 40));
        addBtn.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel stockLabel = new JLabel("Stock: " + p.getStockQuantity());
        stockLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        stockLabel.setForeground(new Color(127, 140, 141));
        stockLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        stockLabel.setHorizontalAlignment(SwingConstants.CENTER);

        addBtn.addActionListener(e -> {
            if (currentUser == null) {
                LoginDialog loginDialog = new LoginDialog(this);
                loginDialog.setVisible(true);
                User loggedIn = loginDialog.getAuthenticatedUser();
                if (loggedIn != null) {
                    this.currentUser = loggedIn;
                    for (Component c : ((JPanel)((BorderLayout)((JPanel)getContentPane().getComponent(0)).getLayout()).getLayoutComponent(BorderLayout.EAST)).getComponents()) {
                        if ("adminBtn".equals(c.getName())) {
                            c.setVisible(currentUser instanceof Admin);
                        }
                    }
                    userLabel.setText("\uD83D\uDC64 " + currentUser.getUsername());
                    Toast.showSuccess(this, "Logged in as " + currentUser.getUsername());
                } else {
                    return;
                }
            }
            if (p.getStockQuantity() == 0) {
                Toast.show(this, "Out of stock!");
                return;
            }
            long count = cart.stream().filter(item -> item.getProductId().equals(p.getProductId())).count();
            if (count >= p.getStockQuantity()) {
                Toast.show(this, "Cannot add more. Only " + p.getStockQuantity() + " in stock.");
                return;
            }
            cart.add(p);
            // Replace invasive popup with our beautiful Toast notification!
            Toast.showSuccess(this, p.getName() + " added to cart!");
        });

        card.add(nameLabel);
        card.add(Box.createRigidArea(new Dimension(0, 8)));
        card.add(descLabel);
        card.add(Box.createRigidArea(new Dimension(0, 15)));
        card.add(pricePanel);
        card.add(Box.createRigidArea(new Dimension(0, 15)));
        card.add(Box.createRigidArea(new Dimension(0, 5)));
        card.add(stockLabel);
        card.add(addBtn);

        return card;
    }

    private JPanel createCartPage() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(245, 247, 250));
        panel.setBorder(new EmptyBorder(30, 50, 30, 50));

        JLabel title = new JLabel("Your Shopping Cart");
        title.setFont(new Font("Segoe UI", Font.BOLD, 28));
        title.setForeground(new Color(44, 62, 80));
        panel.add(title, BorderLayout.NORTH);

        cartItemsPanel = new JPanel();
        cartItemsPanel.setLayout(new BoxLayout(cartItemsPanel, BoxLayout.Y_AXIS));
        cartItemsPanel.setBackground(Color.WHITE);

        JScrollPane scrollPane = new JScrollPane(cartItemsPanel);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(223, 228, 234)));
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.getVerticalScrollBar().setUI(new ModernScrollBarUI()); // Modern scrollbar
        panel.add(scrollPane, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBackground(new Color(245, 247, 250));
        bottomPanel.setBorder(new EmptyBorder(20, 0, 0, 0));

        totalLabel = new JLabel("Total: $0.00");
        totalLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        totalLabel.setForeground(new Color(44, 62, 80));
        bottomPanel.add(totalLabel, BorderLayout.WEST);

        JButton checkoutBtn = new RoundedButton("Proceed to Checkout", new Color(39, 174, 96), new Color(30, 132, 73));
        checkoutBtn.setPreferredSize(new Dimension(200, 50));

        checkoutBtn.addActionListener(e -> {
            if (cart.isEmpty()) {
                Toast.show(this, "Your cart is empty!");
                return;
            }

            Map<String, Integer> cartCounts = new HashMap<>();
            for (Product p : cart) {
                cartCounts.put(p.getProductId(), cartCounts.getOrDefault(p.getProductId(), 0) + 1);
            }

            for (Product p : availableProducts) {
                int requested = cartCounts.getOrDefault(p.getProductId(), 0);
                if (requested > p.getStockQuantity()) {
                    Toast.show(this, "Not enough stock for: " + p.getName() + " (Only " + p.getStockQuantity() + " left)");
                    return;
                }
            }
            String[] options = {"Credit Card", "Cash on Delivery"};
            int choice = JOptionPane.showOptionDialog(this, "Select Payment Method", "Checkout - Payment Simulation", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
            if (choice == JOptionPane.CLOSED_OPTION) return;

            model.PaymentMethod payment;
            if (choice == 0) {
                String card = JOptionPane.showInputDialog(this, "Enter Card Number:");
                if (card == null || card.trim().isEmpty()) return;
                payment = new model.CardPayment(card);
            } else {
                payment = new model.CashPayment();
            }

            double checkoutTotal = 0;
            for (model.Product pItem : cart) checkoutTotal += pItem.getFinalPrice();
            if (!payment.processPayment(checkoutTotal)) {
                ui.components.Toast.show(this, "Payment failed!");
                return;
            }
            String paymentDetails = payment.getPaymentDetails();


            for (Product p : availableProducts) {
                int requested = cartCounts.getOrDefault(p.getProductId(), 0);
                if (requested > 0) {
                    p.setStockQuantity(p.getStockQuantity() - requested);
                }
            }
            DatabaseManager.saveProducts(availableProducts);

            try {
                File receiptDir = new File("receipts");
                if (!receiptDir.exists()) receiptDir.mkdirs();

                String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                String buyer = (currentUser != null ? currentUser.getUsername() : "Guest");
                String filename = "receipt_" + buyer + "_" + timestamp + ".txt";
                File receiptFile = new File(receiptDir, filename);

                try (PrintWriter writer = new PrintWriter(receiptFile)) {
                    writer.println("=========================================");
                    writer.println("           MainFrame STORE RECEIPT         ");
                    writer.println("=========================================");
                    writer.println("Date: " + new Date());
                    writer.println("Buyer: " + buyer);
                    writer.println("Payment Method: " + paymentDetails);
                    writer.println("-----------------------------------------");
                    writer.println(String.format("%-25s %4s %9s", "Item", "Qty", "Price"));
                    writer.println("-----------------------------------------");

                    double total = 0;
                    for (Map.Entry<String, Integer> entry : cartCounts.entrySet()) {
                        Product p = availableProducts.stream().filter(prod -> prod.getProductId().equals(entry.getKey())).findFirst().orElse(null);
                        if (p != null) {
                            double lineTotal = p.getFinalPrice() * entry.getValue();
                            total += lineTotal;
                            String name = p.getName();
                            if (name.length() > 23) name = name.substring(0, 20) + "...";
                            writer.println(String.format("%-25s %4d $%8.2f", name, entry.getValue(), lineTotal));
                        }
                    }
                    writer.println("-----------------------------------------");
                    writer.println(String.format("Total:%34s", String.format("$%.2f", total)));
                    writer.println("=========================================");
                    writer.println("      Thank you for shopping with us!    ");
                }
                Toast.showSuccess(this, "Order Placed! Receipt saved to receipts/" + filename);
            } catch (Exception ex) {
                ex.printStackTrace();
                Toast.showSuccess(this, "Order Placed! Paid: " + totalLabel.getText());
            }

            cart.clear();
            updateCartUI();

            // Re-render store in case we added stock indicators or to refresh
            gridPanel.removeAll();
            for (Product p : availableProducts) {
                gridPanel.add(createProductCard(p));
            }
            gridPanel.revalidate();
            gridPanel.repaint();
        });

        bottomPanel.add(checkoutBtn, BorderLayout.EAST);
        panel.add(bottomPanel, BorderLayout.SOUTH);

        return panel;
    }

    private void updateCartUI() {
        cartItemsPanel.removeAll();
        double total = 0;

        if (cart.isEmpty()) {
            JLabel empty = new JLabel("Cart is empty.");
            empty.setBorder(new EmptyBorder(20, 20, 20, 20));
            empty.setFont(new Font("Segoe UI", Font.PLAIN, 16));
            cartItemsPanel.add(empty);
        } else {
            for (Product p : cart) {
                JPanel item = new JPanel(new BorderLayout());
                item.setBackground(Color.WHITE);
                item.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(236, 240, 241)),
                    new EmptyBorder(15, 20, 15, 20)
                ));

                try {
                    ImageIcon cartIcon = new ImageIcon(p.getImagePath());
                    Image smallImage = cartIcon.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH);
                    JLabel imgLabel = new JLabel(new ImageIcon(smallImage));
                    imgLabel.setBorder(new EmptyBorder(0, 0, 0, 15));

                    JPanel leftPanel = new JPanel(new BorderLayout());
                    leftPanel.setOpaque(false);
                    leftPanel.add(imgLabel, BorderLayout.WEST);

                    JLabel name = new JLabel(p.getName());
                    name.setFont(new Font("Segoe UI", Font.BOLD, 16));
                    leftPanel.add(name, BorderLayout.CENTER);
                    item.add(leftPanel, BorderLayout.WEST);
                } catch (Exception e) {}

                JLabel price = new JLabel(String.format("$%.2f", p.getFinalPrice()));
                price.setFont(new Font("Segoe UI", Font.BOLD, 16));
                price.setForeground(new Color(39, 174, 96));
                item.add(price, BorderLayout.EAST);

                cartItemsPanel.add(item);
                total += p.getFinalPrice();
            }
        }

        totalLabel.setText(String.format("Total: $%.2f", total));
        cartItemsPanel.revalidate();
        cartItemsPanel.repaint();
    }
}
