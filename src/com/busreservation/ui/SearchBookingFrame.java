package com.busreservation.ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import com.busreservation.database.DBConnection;

public class SearchBookingFrame extends JFrame implements ActionListener {

    private JTextField txtBookingId, txtPassenger, txtBus, txtSeat, txtStatus, txtFare;
    private JButton btnSearch, btnClear;

    private final Color COLOR_PRIMARY = new Color(34, 40, 49);
    private final Color COLOR_ACCENT = new Color(0, 173, 181);
    private final Color COLOR_BG = new Color(247, 249, 252);

    public SearchBookingFrame() {
        setTitle("Search Booking");
        setSize(550, 580);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel mainPanel = new JPanel(new BorderLayout(0, 15));
        mainPanel.setBackground(COLOR_BG);
        mainPanel.setBorder(new EmptyBorder(25, 35, 25, 35));

        JLabel lblTitle = new JLabel("LOOKUP RESERVATION", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTitle.setForeground(COLOR_PRIMARY);
        mainPanel.add(lblTitle, BorderLayout.NORTH);

        JPanel contentPanel = new JPanel(new GridBagLayout());
        contentPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 8, 6, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Search Input Top Line Row
        gbc.gridy = 0; gbc.gridx = 0; gbc.weightx = 0.3;
        JLabel lblId = new JLabel("Booking ID:"); lblId.setFont(new Font("Segoe UI", Font.BOLD, 14));
        contentPanel.add(lblId, gbc);
        
        gbc.gridx = 1; gbc.weightx = 0.5;
        txtBookingId = new JTextField();
        styleField(txtBookingId, true);
        contentPanel.add(txtBookingId, gbc);

        gbc.gridx = 2; gbc.weightx = 0.2;
        btnSearch = new JButton("Find");
        styleButton(btnSearch, COLOR_ACCENT, Color.WHITE);
        btnSearch.addActionListener(this);
        contentPanel.add(btnSearch, gbc);

        // Grid Separation Line
        gbc.gridy = 1; gbc.gridx = 0; gbc.gridwidth = 3;
        contentPanel.add(new JSeparator(), gbc);
        gbc.gridwidth = 1;

        // Information Yield Results Rows
        txtPassenger = addOutputRow(contentPanel, "Passenger Name:", gbc, 2);
        txtBus = addOutputRow(contentPanel, "Assigned Bus Vehicle:", gbc, 3);
        txtSeat = addOutputRow(contentPanel, "Seat Identifier:", gbc, 4);
        txtFare = addOutputRow(contentPanel, "Total Base Fare:", gbc, 5);
        txtStatus = addOutputRow(contentPanel, "Booking Status:", gbc, 6);

        mainPanel.add(contentPanel, BorderLayout.CENTER);

        // Reset Actions Panel
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        actionPanel.setOpaque(false);
        btnClear = new JButton("Reset Query Form");
        styleButton(btnClear, new Color(120, 130, 140), Color.WHITE);
        btnClear.setBorder(BorderFactory.createEmptyBorder(8, 30, 8, 30));
        btnClear.addActionListener(this);
        actionPanel.add(btnClear);
        mainPanel.add(actionPanel, BorderLayout.SOUTH);

        add(mainPanel);
        setVisible(true);
    }

    private JTextField addOutputRow(JPanel panel, String labelStr, GridBagConstraints gbc, int row) {
        gbc.gridy = row; gbc.gridx = 0; gbc.weightx = 0.3;
        JLabel label = new JLabel(labelStr);
        label.setFont(new Font("Segoe UI", Font.BOLD, 14));
        panel.add(label, gbc);

        gbc.gridx = 1; gbc.gridwidth = 2; gbc.weightx = 0.7;
        JTextField field = new JTextField();
        styleField(field, false);
        panel.add(field, gbc);
        gbc.gridwidth = 1;
        return field;
    }

    private void styleField(JTextField field, boolean editable) {
        field.setFont(new Font("Segoe UI", editable ? Font.PLAIN : Font.BOLD, 14));
        field.setEditable(editable);
        field.setBackground(editable ? Color.WHITE : new Color(235, 238, 243));
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(205, 210, 215)),
                BorderFactory.createEmptyBorder(6, 8, 6, 8)
        ));
    }

    private void styleButton(JButton btn, Color bg, Color fg) {
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setBackground(bg);
        btn.setForeground(fg);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnSearch) {
            try {
                Connection con = DBConnection.getConnection();
                String sql = "SELECT p.name, b.bus_name, r.seat_number, r.fare, r.status " +
                             "FROM reservations r JOIN passengers p ON r.passenger_id=p.passenger_id " +
                             "JOIN buses b ON r.bus_id=b.bus_id WHERE r.booking_id=?";
                PreparedStatement ps = con.prepareStatement(sql);
                ps.setInt(1, Integer.parseInt(txtBookingId.getText()));
                ResultSet rs = ps.executeQuery();

                if (rs.next()) {
                    txtPassenger.setText(rs.getString("name"));
                    txtBus.setText(rs.getString("bus_name"));
                    txtSeat.setText(String.valueOf(rs.getInt("seat_number")));
                    txtFare.setText("$" + rs.getDouble("fare"));
                    String status = rs.getString("status");
                    txtStatus.setText((status == null || status.trim().isEmpty()) ? "Booked" : status);
                } else {
                    JOptionPane.showMessageDialog(this, "Booking Not Found", "Information", JOptionPane.INFORMATION_MESSAGE);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage());
            }
        }
        if (e.getSource() == btnClear) {
            txtBookingId.setText("");
            txtPassenger.setText("");
            txtBus.setText("");
            txtSeat.setText("");
            txtFare.setText("");
            txtStatus.setText("");
        }
    }
}