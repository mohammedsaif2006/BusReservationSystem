package com.busreservation.ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import com.busreservation.database.DBConnection;

public class CancelTicketFrame extends JFrame implements ActionListener {

    private JTextField txtBookingId, txtPassenger, txtBus, txtSeat, txtStatus;
    private JButton btnSearch, btnCancel, btnClear;
    private int busId = 0;

    private final Color COLOR_PRIMARY = new Color(34, 40, 49);
    private final Color COLOR_ACCENT = new Color(0, 173, 181);
    private final Color COLOR_BG = new Color(247, 249, 252);
    private final Color COLOR_DANGER = new Color(211, 47, 47);

    public CancelTicketFrame() {
        setTitle("Cancel Ticket");
        setSize(550, 580);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel mainPanel = new JPanel(new BorderLayout(0, 15));
        mainPanel.setBackground(COLOR_BG);
        mainPanel.setBorder(new EmptyBorder(25, 35, 25, 35));

        JLabel lblTitle = new JLabel("CANCEL RESERVATION", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTitle.setForeground(COLOR_PRIMARY);
        mainPanel.add(lblTitle, BorderLayout.NORTH);

        JPanel contentPanel = new JPanel(new GridBagLayout());
        contentPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 8, 6, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Search Section Row
        gbc.gridy = 0; gbc.gridx = 0; gbc.weightx = 0.3;
        JLabel lblId = new JLabel("Booking ID:"); lblId.setFont(new Font("Segoe UI", Font.BOLD, 14));
        contentPanel.add(lblId, gbc);
        
        gbc.gridx = 1; gbc.weightx = 0.5;
        txtBookingId = new JTextField();
        styleField(txtBookingId, true);
        contentPanel.add(txtBookingId, gbc);

        gbc.gridx = 2; gbc.weightx = 0.2;
        btnSearch = new JButton("Search");
        styleButton(btnSearch, COLOR_PRIMARY, Color.WHITE);
        btnSearch.addActionListener(this);
        contentPanel.add(btnSearch, gbc);

        // Divider
        gbc.gridy = 1; gbc.gridx = 0; gbc.gridwidth = 3;
        contentPanel.add(new JSeparator(), gbc);
        gbc.gridwidth = 1; // reset

        // Read-only populated info rows
        txtPassenger = addInfoRow(contentPanel, "Passenger Name:", gbc, 2);
        txtBus = addInfoRow(contentPanel, "Assigned Bus:", gbc, 3);
        txtSeat = addInfoRow(contentPanel, "Seat Assignment:", gbc, 4);
        txtStatus = addInfoRow(contentPanel, "Current Status:", gbc, 5);

        mainPanel.add(contentPanel, BorderLayout.CENTER);

        // Footer Actions
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        actionPanel.setOpaque(false);

        btnCancel = new JButton("Cancel Ticket");
        styleButton(btnCancel, COLOR_DANGER, Color.WHITE);
        btnClear = new JButton("Clear");
        styleButton(btnClear, new Color(120, 130, 140), Color.WHITE);

        btnCancel.addActionListener(this);
        btnClear.addActionListener(this);
        actionPanel.add(btnCancel);
        actionPanel.add(btnClear);
        mainPanel.add(actionPanel, BorderLayout.SOUTH);

        add(mainPanel);
        setVisible(true);
    }

    private JTextField addInfoRow(JPanel panel, String txt, GridBagConstraints gbc, int row) {
        gbc.gridy = row; gbc.gridx = 0; gbc.weightx = 0.3;
        JLabel lbl = new JLabel(txt);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 14));
        panel.add(lbl, gbc);

        gbc.gridx = 1; gbc.gridwidth = 2; gbc.weightx = 0.7;
        JTextField field = new JTextField();
        styleField(field, false);
        panel.add(field, gbc);
        gbc.gridwidth = 1; // reset
        return field;
    }

    private void styleField(JTextField field, boolean editable) {
        field.setFont(new Font("Segoe UI", editable ? Font.PLAIN : Font.BOLD, 14));
        field.setEditable(editable);
        field.setBackground(editable ? Color.WHITE : new Color(230, 235, 240));
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 205, 210)),
                BorderFactory.createEmptyBorder(5, 8, 5, 8)
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
                String sql = "SELECT r.bus_id, p.name, b.bus_name, r.seat_number, r.status " +
                             "FROM reservations r JOIN passengers p ON r.passenger_id = p.passenger_id " +
                             "JOIN buses b ON r.bus_id = b.bus_id WHERE r.booking_id = ?";
                PreparedStatement ps = con.prepareStatement(sql);
                ps.setInt(1, Integer.parseInt(txtBookingId.getText()));
                ResultSet rs = ps.executeQuery();

                if (rs.next()) {
                    busId = rs.getInt("bus_id");
                    txtPassenger.setText(rs.getString("name"));
                    txtBus.setText(rs.getString("bus_name"));
                    txtSeat.setText(String.valueOf(rs.getInt("seat_number")));
                    String status = rs.getString("status");
                    txtStatus.setText((status == null || status.trim().isEmpty()) ? "Booked" : status);
                } else {
                    JOptionPane.showMessageDialog(this, "Booking Not Found", "Alert", JOptionPane.WARNING_MESSAGE);
                    clearForm();
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage());
            }
        }
        if (e.getSource() == btnCancel) {
            try {
                if (txtBookingId.getText().trim().isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Enter Booking ID."); return;
                }
                if (txtPassenger.getText().trim().isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Search the booking first."); return;
                }
                if (txtStatus.getText().equalsIgnoreCase("Cancelled")) {
                    JOptionPane.showMessageDialog(this, "Ticket is already cancelled."); return;
                }

                Connection con = DBConnection.getConnection();
                PreparedStatement ps = con.prepareStatement("UPDATE reservations SET status=? WHERE booking_id=?");
                ps.setString(1, "Cancelled");
                ps.setInt(2, Integer.parseInt(txtBookingId.getText()));
                int rows = ps.executeUpdate();

                if (rows > 0) {
                    PreparedStatement ps2 = con.prepareStatement("UPDATE buses SET available_seats = available_seats + 1 WHERE bus_id=?");
                    ps2.setInt(1, busId);
                    ps2.executeUpdate();
                    txtStatus.setText("Cancelled");
                    JOptionPane.showMessageDialog(this, "Ticket Cancelled Successfully!");
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage());
            }
        }
        if (e.getSource() == btnClear) {
            clearForm();
            txtBookingId.setText("");
        }
    }

    private void clearForm() {
        txtPassenger.setText("");
        txtBus.setText("");
        txtSeat.setText("");
        txtStatus.setText("");
        busId = 0;
    }
}