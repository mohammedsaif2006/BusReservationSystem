package com.busreservation.ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.sql.*;
import com.busreservation.database.DBConnection;

public class ViewBookingsFrame extends JFrame {

    private JTable table;
    private DefaultTableModel model;

    public ViewBookingsFrame() {
        setTitle("View Bookings");
        setSize(950, 520);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(247, 249, 252));
        mainPanel.setBorder(new EmptyBorder(20, 25, 20, 25));

        JLabel titleLabel = new JLabel("RESERVATION MASTER LEDGER", SwingConstants.LEFT);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(new Color(34, 40, 49));
        titleLabel.setBorder(new EmptyBorder(0, 0, 15, 0));
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        model = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };

        model.addColumn("Booking ID");
        model.addColumn("Passenger");
        model.addColumn("Bus Vehicle");
        model.addColumn("Seat");
        model.addColumn("Fare Paid");
        model.addColumn("Booking Date");
        model.addColumn("Status");

        table = new JTable(model);
        styleTable(table);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(225, 230, 235)));
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        loadBookings();
        add(mainPanel);
        setVisible(true);
    }

    private void styleTable(JTable table) {
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.setRowHeight(32);
        table.setGridColor(new Color(235, 238, 242));
        table.setSelectionBackground(new Color(0, 173, 181, 40));
        table.setSelectionForeground(Color.BLACK);

        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 14));
        header.setBackground(new Color(34, 40, 49));
        header.setForeground(Color.WHITE);
        header.setPreferredSize(new Dimension(100, 40));
    }

    private void loadBookings() {
        try {
            Connection con = DBConnection.getConnection();
            String sql = "SELECT r.booking_id, p.name, b.bus_name, r.seat_number, r.fare, r.booking_date, r.status " +
                         "FROM reservations r JOIN passengers p ON r.passenger_id = p.passenger_id " +
                         "JOIN buses b ON r.bus_id = b.bus_id";
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(sql);

            while (rs.next()) {
                String status = rs.getString("status");
                if (status == null || status.trim().isEmpty()) {
                    status = "Booked";
                }
                model.addRow(new Object[]{
                    rs.getInt("booking_id"),
                    rs.getString("name"),
                    rs.getString("bus_name"),
                    rs.getInt("seat_number"),
                    "$" + rs.getDouble("fare"),
                    rs.getDate("booking_date"),
                    status
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }
}