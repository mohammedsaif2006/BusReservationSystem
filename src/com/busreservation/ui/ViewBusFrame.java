package com.busreservation.ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.sql.*;
import com.busreservation.database.DBConnection;

public class ViewBusFrame extends JFrame {

    private JTable table;
    private DefaultTableModel model;

    public ViewBusFrame() {
        setTitle("View Buses");
        setSize(800, 480);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(247, 249, 252));
        mainPanel.setBorder(new EmptyBorder(20, 25, 20, 25));

        JLabel titleLabel = new JLabel("OPERATIONAL FLEET REGISTRY", SwingConstants.LEFT);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(new Color(34, 40, 49));
        titleLabel.setBorder(new EmptyBorder(0, 0, 15, 0));
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        model = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };

        model.addColumn("Bus ID");
        model.addColumn("Bus Name");
        model.addColumn("Source Station");
        model.addColumn("Destination Station");
        model.addColumn("Fare Rate");
        model.addColumn("Available Seats");

        table = new JTable(model);
        styleTable(table);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(225, 230, 235)));
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        loadBuses();
        add(mainPanel);
        setVisible(true);
    }

    private void styleTable(JTable table) {
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.setRowHeight(30);
        table.setGridColor(new Color(235, 238, 242));
        table.setSelectionBackground(new Color(0, 173, 181, 40));
        table.setSelectionForeground(Color.BLACK);

        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 14));
        header.setBackground(new Color(34, 40, 49));
        header.setForeground(Color.WHITE);
        header.setPreferredSize(new Dimension(100, 38));
    }

    private void loadBuses() {
        try {
            Connection con = DBConnection.getConnection();
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM buses");
            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getInt("bus_id"),
                    rs.getString("bus_name"),
                    rs.getString("source"),
                    rs.getString("destination"),
                    "$" + rs.getDouble("fare"),
                    rs.getInt("available_seats")
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }
}