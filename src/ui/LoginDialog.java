package ui;

import model.User;
import util.DatabaseManager;
import ui.components.RoundedButton;
import ui.components.RoundedPanel;
import ui.components.Toast;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;

public class LoginDialog extends JDialog {
    private List<User> users;
    private User authenticatedUser = null;

    private JPanel cardPanel;
    private CardLayout cardLayout;

    public LoginDialog(Frame parent) {
        super(parent, "E-Commerce Account", true);
        users = DatabaseManager.loadUsers();

        setSize(450, 600);
        setLocationRelativeTo(parent);
        getContentPane().setBackground(new Color(245, 247, 250));

        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);
        cardPanel.setBackground(new Color(245, 247, 250));

        cardPanel.add(createLoginPanel(), "LOGIN");
        cardPanel.add(createRegisterPanel(), "REGISTER");

        add(cardPanel);
    }

    private JPanel createLoginPanel() {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(new EmptyBorder(40, 40, 40, 40));
        mainPanel.setBackground(new Color(245, 247, 250));

        JLabel titleLabel = new JLabel("Welcome Back");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(new Color(44, 62, 80));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subTitleLabel = new JLabel("Please sign in to proceed");
        subTitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subTitleLabel.setForeground(new Color(127, 140, 141));
        subTitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        RoundedPanel formPanel = new RoundedPanel();
        formPanel.setLayout(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(new EmptyBorder(30, 30, 30, 30));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        gbc.gridx = 0;

        // Username
        JLabel userLabel = new JLabel("Username");
        userLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        userLabel.setForeground(new Color(44, 62, 80));
        gbc.gridy = 0; gbc.insets = new Insets(0, 0, 5, 0);
        formPanel.add(userLabel, gbc);

        JTextField usernameField = createTextField();
        gbc.gridy = 1; gbc.insets = new Insets(0, 0, 15, 0);
        formPanel.add(usernameField, gbc);

        // Password
        JLabel passLabel = new JLabel("Password");
        passLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        passLabel.setForeground(new Color(44, 62, 80));
        gbc.gridy = 2; gbc.insets = new Insets(0, 0, 5, 0);
        formPanel.add(passLabel, gbc);

        JPasswordField passwordField = createPasswordField();
        gbc.gridy = 3; gbc.insets = new Insets(0, 0, 25, 0);
        formPanel.add(passwordField, gbc);

        // Login Button
        JButton loginButton = new RoundedButton("Sign In", new Color(52, 152, 219), new Color(41, 128, 185));
        loginButton.setPreferredSize(new Dimension(0, 45));
        gbc.gridy = 4; gbc.insets = new Insets(0, 0, 15, 0);
        formPanel.add(loginButton, gbc);

        // Switch to Register
        JButton switchBtn = createLinkButton("Don't have an account? Sign Up");
        gbc.gridy = 5; gbc.insets = new Insets(0, 0, 0, 0);
        formPanel.add(switchBtn, gbc);

        loginButton.addActionListener(e -> {
            String username = usernameField.getText().trim();
            String password = new String(passwordField.getPassword());
            User loggedInUser = null;
            for (User u : users) {
                if (u.getUsername().equals(username) && u.getPassword().equals(password)) {
                    loggedInUser = u; break;
                }
            }
            if (loggedInUser != null) {
                this.authenticatedUser = loggedInUser;
                this.dispose();
            } else {
                Toast.show(this, "Invalid credentials.");
            }
        });

        switchBtn.addActionListener(e -> cardLayout.show(cardPanel, "REGISTER"));

        mainPanel.add(titleLabel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        mainPanel.add(subTitleLabel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 25)));
        mainPanel.add(formPanel);

        return mainPanel;
    }

    private JPanel createRegisterPanel() {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(new EmptyBorder(25, 40, 25, 40));
        mainPanel.setBackground(new Color(245, 247, 250));

        JLabel titleLabel = new JLabel("Create Account");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(new Color(44, 62, 80));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subTitleLabel = new JLabel("Join us today");
        subTitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subTitleLabel.setForeground(new Color(127, 140, 141));
        subTitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        RoundedPanel formPanel = new RoundedPanel();
        formPanel.setLayout(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(new EmptyBorder(25, 30, 25, 30));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        gbc.gridx = 0;

        // Username
        JLabel userLabel = new JLabel("Username");
        userLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        userLabel.setForeground(new Color(44, 62, 80));
        gbc.gridy = 0; gbc.insets = new Insets(0, 0, 5, 0);
        formPanel.add(userLabel, gbc);

        JTextField usernameField = createTextField();
        gbc.gridy = 1; gbc.insets = new Insets(0, 0, 10, 0);
        formPanel.add(usernameField, gbc);

        // Email
        JLabel emailLabel = new JLabel("Email");
        emailLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        emailLabel.setForeground(new Color(44, 62, 80));
        gbc.gridy = 2; gbc.insets = new Insets(0, 0, 5, 0);
        formPanel.add(emailLabel, gbc);

        JTextField emailField = createTextField();
        gbc.gridy = 3; gbc.insets = new Insets(0, 0, 10, 0);
        formPanel.add(emailField, gbc);

        // Password
        JLabel passLabel = new JLabel("Password");
        passLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        passLabel.setForeground(new Color(44, 62, 80));
        gbc.gridy = 4; gbc.insets = new Insets(0, 0, 5, 0);
        formPanel.add(passLabel, gbc);

        JPasswordField passwordField = createPasswordField();
        gbc.gridy = 5; gbc.insets = new Insets(0, 0, 20, 0);
        formPanel.add(passwordField, gbc);

        // Register Button
        JButton registerButton = new RoundedButton("Create Account", new Color(39, 174, 96), new Color(30, 132, 73));
        registerButton.setPreferredSize(new Dimension(0, 45));
        gbc.gridy = 6; gbc.insets = new Insets(0, 0, 15, 0);
        formPanel.add(registerButton, gbc);

        // Switch to Login
        JButton switchBtn = createLinkButton("Already have an account? Sign In");
        gbc.gridy = 7; gbc.insets = new Insets(0, 0, 0, 0);
        formPanel.add(switchBtn, gbc);

        registerButton.addActionListener(e -> {
            String username = usernameField.getText().trim();
            String email = emailField.getText().trim();
            String password = new String(passwordField.getPassword());

            if (username.isEmpty() || email.isEmpty() || password.isEmpty()) {
                Toast.show(this, "Please fill in all fields.");
                return;
            }

            for (User u : users) {
                if (u.getUsername().equalsIgnoreCase(username)) {
                    Toast.show(this, "Username already exists.");
                    return;
                }
            }

            String newId = "U" + String.format("%03d", users.size() + 1);
            User newUser = new User(newId, username, password, email);
            users.add(newUser);
            DatabaseManager.saveUsers(users);

            this.authenticatedUser = newUser;
            this.dispose();
        });

        switchBtn.addActionListener(e -> cardLayout.show(cardPanel, "LOGIN"));

        mainPanel.add(titleLabel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        mainPanel.add(subTitleLabel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        mainPanel.add(formPanel);

        return mainPanel;
    }

    private JTextField createTextField() {
        JTextField field = new JTextField();
        field.setPreferredSize(new Dimension(0, 40));
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(223, 228, 234)),
                new EmptyBorder(5, 10, 5, 10)
        ));
        return field;
    }

    private JPasswordField createPasswordField() {
        JPasswordField field = new JPasswordField();
        field.setPreferredSize(new Dimension(0, 40));
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(223, 228, 234)),
                new EmptyBorder(5, 10, 5, 10)
        ));
        return field;
    }

    private JButton createLinkButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btn.setForeground(new Color(52, 152, 219));
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    public User getAuthenticatedUser() {
        return authenticatedUser;
    }
}
