package com.busreservation.ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import com.busreservation.database.DBConnection;

public class AddBusFrame extends JFrame implements ActionListener {

    private JTextField txtBusName, txtSource, txtDestination, txtFare, txtSeats;
    private JButton btnSave, btnClear, btnBack;

    private final Color COLOR_PRIMARY = new Color(34, 40, 49);
    private final Color COLOR_ACCENT = new Color(0, 173, 181);
    private final Color COLOR_BG = new Color(247, 249, 252);

    public AddBusFrame() {
        setTitle("Add Bus");
        setSize(550, 550);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(COLOR_BG);
        mainPanel.setBorder(new EmptyBorder(20, 40, 30, 40));

        // Modern Top Bar
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setOpaque(false);
        btnBack = new JButton("← Back");
        btnBack.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnBack.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnBack.addActionListener(e -> dispose());
        
        JLabel heading = new JLabel("ADD NEW BUS", SwingConstants.CENTER);
        heading.setFont(new Font("Segoe UI", Font.BOLD, 20));
        heading.setForeground(COLOR_PRIMARY);
        
        topBar.add(btnBack, BorderLayout.WEST);
        topBar.add(heading, BorderLayout.CENTER);
        mainPanel.add(topBar, BorderLayout.NORTH);

        // Form Layout Grid
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        txtBusName = addRow(formPanel, "Bus Name:", gbc, 0);
        txtSource = addRow(formPanel, "Source:", gbc, 1);
        txtDestination = addRow(formPanel, "Destination:", gbc, 2);
        txtFare = addRow(formPanel, "Fare ($):", gbc, 3);
        txtSeats = addRow(formPanel, "Total Seats:", gbc, 4);

        mainPanel.add(formPanel, BorderLayout.CENTER);

        // Button Row
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        actionPanel.setOpaque(false);

        btnSave = new JButton("Save Bus Details");
        styleButton(btnSave, COLOR_ACCENT, Color.WHITE);
        btnClear = new JButton("Clear Form");
        styleButton(btnClear, new Color(120, 130, 140), Color.WHITE);

        btnSave.addActionListener(this);
        btnClear.addActionListener(this);

        actionPanel.add(btnSave);
        actionPanel.add(btnClear);
        mainPanel.add(actionPanel, BorderLayout.SOUTH);

        add(mainPanel);
        setVisible(true);
    }

    private JTextField addRow(JPanel panel, String labelText, GridBagConstraints gbc, int row) {
        gbc.gridy = row;
        gbc.gridx = 0;
        gbc.weightx = 0.3;
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Segoe UI", Font.BOLD, 14));
        panel.add(label, gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.7;
        JTextField field = new JTextField();
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(210, 215, 223), 1),
                BorderFactory.createEmptyBorder(6, 10, 6, 10)
        ));
        panel.add(field, gbc);
        return field;
    }

    private void styleButton(JButton button, Color bg, Color fg) {
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setBackground(bg);
        button.setForeground(fg);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setBorder(BorderFactory.createEmptyBorder(10, 25, 10, 25));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnSave) {
            try {
                Connection con = DBConnection.getConnection();
                String sql = "INSERT INTO buses(bus_name,source,destination,fare,total_seats,available_seats) VALUES(?,?,?,?,?,?)";
                PreparedStatement ps = con.prepareStatement(sql);
                ps.setString(1, txtBusName.getText());
                ps.setString(2, txtSource.getText());
                ps.setString(3, txtDestination.getText());
                ps.setDouble(4, Double.parseDouble(txtFare.getText()));
                ps.setInt(5, Integer.parseInt(txtSeats.getText()));
                ps.setInt(6, Integer.parseInt(txtSeats.getText()));

                ps.executeUpdate();
                JOptionPane.showMessageDialog(this, "Bus Added Successfully!");
                clearFields();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Registration Error", JOptionPane.ERROR_MESSAGE);
            }
        }
        if (e.getSource() == btnClear) {
            clearFields();
        }
    }

    private void clearFields() {
        txtBusName.setText("");
        txtSource.setText("");
        txtDestination.setText("");
        txtFare.setText("");
        txtSeats.setText("");
    }
}