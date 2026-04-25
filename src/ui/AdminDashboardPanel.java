package ui;

import model.Admin;
import model.Product;
import model.User;
import util.DatabaseManager;
import ui.components.RoundedButton;
import ui.components.Toast;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import model.Electronics;
import model.Clothing;
import model.StandardProduct;

public class AdminDashboardPanel extends JPanel {
    private List<Product> products;
    private List<User> users;
    
    private JTable productTable;
    private DefaultTableModel productTableModel;
    
    private JTable userTable;
    private DefaultTableModel userTableModel;

    public AdminDashboardPanel() {
        setLayout(new BorderLayout());
        setBackground(new Color(245, 247, 250));
        setBorder(new EmptyBorder(20, 30, 20, 30));

        products = DatabaseManager.loadProducts();
        users = DatabaseManager.loadUsers();

        JLabel title = new JLabel("Admin Dashboard");
        title.setFont(new Font("Segoe UI", Font.BOLD, 28));
        title.setForeground(new Color(44, 62, 80));
        title.setBorder(new EmptyBorder(0, 0, 20, 0));
        add(title, BorderLayout.NORTH);

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        
        tabbedPane.addTab("Manage Products", createProductPanel());
        tabbedPane.addTab("Manage Users", createUserPanel());

        add(tabbedPane, BorderLayout.CENTER);
    }

    private JPanel createProductPanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 15));
        panel.setOpaque(false);
        panel.setBorder(new EmptyBorder(15, 15, 15, 15));

        String[] columns = {"ID", "Name", "Category", "Price", "Stock", "Image Path"};
        productTableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        
        refreshProductTable();

        productTable = new JTable(productTableModel);
        productTable.setRowHeight(30);
        productTable.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        productTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        
        JScrollPane scrollPane = new JScrollPane(productTable);
        panel.add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonPanel.setOpaque(false);

        JButton addBtn = new RoundedButton("Add Product", new Color(46, 204, 113), new Color(39, 174, 96));
        JButton editBtn = new RoundedButton("Edit Product", new Color(52, 152, 219), new Color(41, 128, 185));
        JButton delBtn = new RoundedButton("Delete Product", new Color(231, 76, 60), new Color(192, 57, 43));
        
        Dimension btnSize = new Dimension(130, 40);
        addBtn.setPreferredSize(btnSize);
        editBtn.setPreferredSize(btnSize);
        delBtn.setPreferredSize(btnSize);

        addBtn.addActionListener(e -> showProductDialog(null));
        editBtn.addActionListener(e -> {
            int row = productTable.getSelectedRow();
            if (row < 0) {
                Toast.show(SwingUtilities.getWindowAncestor(this), "Select a product to edit.");
                return;
            }
            Product p = products.get(row);
            showProductDialog(p);
        });
        delBtn.addActionListener(e -> {
            int row = productTable.getSelectedRow();
            if (row < 0) {
                Toast.show(SwingUtilities.getWindowAncestor(this), "Select a product to delete.");
                return;
            }
            int confirm = JOptionPane.showConfirmDialog(this, "Are you sure?", "Delete Product", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                products.remove(row);
                DatabaseManager.saveProducts(products);
                refreshProductTable();
                Toast.showSuccess(SwingUtilities.getWindowAncestor(this), "Product deleted.");
            }
        });

        buttonPanel.add(addBtn);
        buttonPanel.add(editBtn);
        buttonPanel.add(delBtn);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        return panel;
    }

    private void refreshProductTable() {
        productTableModel.setRowCount(0);
        for (Product p : products) {
            productTableModel.addRow(new Object[]{p.getProductId(), p.getName(), p.getCategory(), p.getPrice(), p.getStockQuantity(), p.getImagePath()});
        }
    }

    private void showProductDialog(Product p) {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), p == null ? "Add Product" : "Edit Product", true);
        dialog.setSize(400, 350);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());

        JPanel form = new JPanel(new GridLayout(7, 2, 10, 10));
        form.setBorder(new EmptyBorder(20, 20, 20, 20));

        JTextField idField = new JTextField(p != null ? p.getProductId() : "");
        JTextField nameField = new JTextField(p != null ? p.getName() : "");
        JTextField descField = new JTextField(p != null ? p.getDescription() : "");
        JTextField priceField = new JTextField(p != null ? String.valueOf(p.getPrice()) : "");
        JTextField stockField = new JTextField(p != null ? String.valueOf(p.getStockQuantity()) : "");
        JTextField imgField = new JTextField(p != null ? p.getImagePath() : "images/prod1.png");

        if (p != null) idField.setEditable(false);
        JComboBox<String> catBox = new JComboBox<>(new String[]{"Standard", "Electronics", "Clothing"});
        if (p != null) catBox.setSelectedItem(p.getCategory());
        if (p != null) catBox.setEnabled(false);

        form.add(new JLabel("Product ID:")); form.add(idField);
        form.add(new JLabel("Name:")); form.add(nameField);
        form.add(new JLabel("Description:")); form.add(descField);
        form.add(new JLabel("Category:")); form.add(catBox);
        form.add(new JLabel("Price:")); form.add(priceField);
        form.add(new JLabel("Stock:")); form.add(stockField);
        form.add(new JLabel("Image Path:")); form.add(imgField);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton saveBtn = new RoundedButton("Save", new Color(46, 204, 113), new Color(39, 174, 96));
        saveBtn.setPreferredSize(new Dimension(100, 35));
        saveBtn.addActionListener(e -> {
            try {
                String id = idField.getText();
                String name = nameField.getText();
                String desc = descField.getText();
                double price = Double.parseDouble(priceField.getText());
                int stock = Integer.parseInt(stockField.getText());
                String img = imgField.getText();

                if (p == null) {
                    String cat = (String) catBox.getSelectedItem();
                    if ("Electronics".equals(cat)) products.add(new Electronics(id, name, desc, price, stock, img));
                    else if ("Clothing".equals(cat)) products.add(new Clothing(id, name, desc, price, stock, img));
                    else products.add(new StandardProduct(id, name, desc, price, stock, img));
                } else {
                    p.setName(name);
                    p.setDescription(desc);
                    p.setPrice(price);
                    p.setStockQuantity(stock);
                    p.setImagePath(img);
                }
                DatabaseManager.saveProducts(products);
                refreshProductTable();
                dialog.dispose();
                Toast.showSuccess(SwingUtilities.getWindowAncestor(this), "Product saved successfully!");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Invalid input. Please check numbers.");
            }
        });
        btnPanel.add(saveBtn);

        dialog.add(form, BorderLayout.CENTER);
        dialog.add(btnPanel, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    private JPanel createUserPanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 15));
        panel.setOpaque(false);
        panel.setBorder(new EmptyBorder(15, 15, 15, 15));

        String[] columns = {"User ID", "Username", "Email", "Role"};
        userTableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        
        refreshUserTable();

        userTable = new JTable(userTableModel);
        userTable.setRowHeight(30);
        userTable.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        userTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        
        JScrollPane scrollPane = new JScrollPane(userTable);
        panel.add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonPanel.setOpaque(false);

        JButton delBtn = new RoundedButton("Delete User", new Color(231, 76, 60), new Color(192, 57, 43));
        delBtn.setPreferredSize(new Dimension(130, 40));
        delBtn.addActionListener(e -> {
            int row = userTable.getSelectedRow();
            if (row < 0) {
                Toast.show(SwingUtilities.getWindowAncestor(this), "Select a user to delete.");
                return;
            }
            User u = users.get(row);
            if (u instanceof Admin) {
                Toast.show(SwingUtilities.getWindowAncestor(this), "Cannot delete an Admin account.");
                return;
            }
            int confirm = JOptionPane.showConfirmDialog(this, "Are you sure?", "Delete User", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                users.remove(row);
                DatabaseManager.saveUsers(users);
                refreshUserTable();
                Toast.showSuccess(SwingUtilities.getWindowAncestor(this), "User deleted.");
            }
        });

        buttonPanel.add(delBtn);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        return panel;
    }

    private void refreshUserTable() {
        userTableModel.setRowCount(0);
        for (User u : users) {
            String role = (u instanceof Admin) ? "Admin" : "Customer";
            userTableModel.addRow(new Object[]{u.getUserId(), u.getUsername(), u.getEmail(), role});
        }
    }
}
