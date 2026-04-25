package ui.components;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Toast extends JWindow {
    public Toast(Window parent, String message, Color bgColor) {
        super(parent);
        setBackground(new Color(0, 0, 0, 0)); // Transparent background
        
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(bgColor);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                g2.dispose();
            }
        };
        panel.setOpaque(false);
        panel.setBorder(new EmptyBorder(12, 24, 12, 24));
        
        JLabel label = new JLabel(message);
        label.setFont(new Font("Segoe UI", Font.BOLD, 14));
        label.setForeground(Color.WHITE);
        panel.add(label);
        
        add(panel);
        pack();
        
        if (parent != null) {
            int x = parent.getX() + (parent.getWidth() - getWidth()) / 2;
            int y = parent.getY() + parent.getHeight() - 100;
            setLocation(x, y);
        }
        
        Timer timer = new Timer(3000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        timer.setRepeats(false);
        timer.start();
    }

    public static void show(Window parent, String message) {
        new Toast(parent, message, new Color(44, 62, 80, 230)).setVisible(true);
    }
    
    public static void showSuccess(Window parent, String message) {
        new Toast(parent, message, new Color(39, 174, 96, 230)).setVisible(true);
    }
}
