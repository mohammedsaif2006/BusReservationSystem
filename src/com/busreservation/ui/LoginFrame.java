package com.busreservation.ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import com.busreservation.database.DBConnection;

public class LoginFrame extends JFrame implements ActionListener {

    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JButton btnLogin, btnExit;

    // Palette Match
    private final Color COLOR_PRIMARY = new Color(34, 40, 49);
    private final Color COLOR_ACCENT = new Color(0, 173, 181);
    private final Color COLOR_BG = new Color(247, 249, 252);
    private final Color COLOR_TEXT_DARK = new Color(43, 43, 43);

    public LoginFrame() {
        setTitle("Bus Ticket Reservation System");
        setSize(450, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        // Main elegant container
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(COLOR_BG);
        mainPanel.setBorder(new EmptyBorder(30, 40, 30, 40));

        // Header Title
        JLabel titleLabel = new JLabel("TRANSIT PRO LOGIN", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        titleLabel.setForeground(COLOR_PRIMARY);
        titleLabel.setBorder(new EmptyBorder(0, 0, 25, 0));
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        // Form Center Area
        JPanel formPanel = new JPanel(new GridLayout(4, 1, 10, 10));
        formPanel.setOpaque(false);

        JLabel userLabel = new JLabel("Username");
        userLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        userLabel.setForeground(COLOR_TEXT_DARK);
        txtUsername = new JTextField();
        styleField(txtUsername);

        JLabel passLabel = new JLabel("Password");
        passLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        passLabel.setForeground(COLOR_TEXT_DARK);
        txtPassword = new JPasswordField();
        styleField(txtPassword);

        formPanel.add(userLabel);
        formPanel.add(txtUsername);
        formPanel.add(passLabel);
        formPanel.add(txtPassword);
        mainPanel.add(formPanel, BorderLayout.CENTER);

        // Bottom Actions Pane
        JPanel actionPanel = new JPanel(new GridLayout(1, 2, 15, 0));
        actionPanel.setOpaque(false);
        actionPanel.setBorder(new EmptyBorder(25, 0, 10, 0));

        btnLogin = createStyledButton("Login", COLOR_ACCENT, Color.WHITE);
        btnExit = createStyledButton("Exit", new Color(211, 47, 47), Color.WHITE);

        btnLogin.addActionListener(this);
        btnExit.addActionListener(this);

        actionPanel.add(btnLogin);
        actionPanel.add(btnExit);
        mainPanel.add(actionPanel, BorderLayout.SOUTH);

        add(mainPanel);
        setVisible(true);
    }

    private void styleField(JTextField field) {
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
    }

    private JButton createStyledButton(String text, Color bg, Color fg) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setBackground(bg);
        btn.setForeground(fg);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        return btn;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnLogin) {
            String username = txtUsername.getText();
            String password = String.valueOf(txtPassword.getPassword());
            try {
                Connection con = DBConnection.getConnection();
                String sql = "SELECT * FROM login WHERE username=? AND password=?";
                PreparedStatement ps = con.prepareStatement(sql);
                ps.setString(1, username);
                ps.setString(2, password);
                ResultSet rs = ps.executeQuery();

                if (rs.next()) {
                    JOptionPane.showMessageDialog(this, "Login Successful!");
                    dispose();
                    new DashboardFrame();
                } else {
                    JOptionPane.showMessageDialog(this, "Invalid Username or Password", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        if (e.getSource() == btnExit) {
            System.exit(0);
        }
    }
}