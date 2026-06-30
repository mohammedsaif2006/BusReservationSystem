package com.busreservation.ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.time.LocalDate;
import com.busreservation.database.DBConnection;

public class BookTicketFrame extends JFrame implements ActionListener {

    private JTextField txtName, txtAge, txtPhone, txtSeat;
    private JComboBox<String> cmbGender, cmbBus;
    private JButton btnBook, btnClear, btnBack;

    private final Color COLOR_PRIMARY = new Color(34, 40, 49);
    private final Color COLOR_ACCENT = new Color(0, 173, 181);
    private final Color COLOR_BG = new Color(247, 249, 252);

    public BookTicketFrame() {
        setTitle("Book Ticket");
        setSize(580, 620);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(COLOR_BG);
        mainPanel.setBorder(new EmptyBorder(20, 40, 25, 40));

        // Top Navigation Header
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setOpaque(false);
        btnBack = new JButton("← Back");
        btnBack.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnBack.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnBack.addActionListener(e -> dispose());

        JLabel heading = new JLabel("PASSENGER TICKET BOOKING", SwingConstants.CENTER);
        heading.setFont(new Font("Segoe UI", Font.BOLD, 18));
        heading.setForeground(COLOR_PRIMARY);

        topBar.add(btnBack, BorderLayout.WEST);
        topBar.add(heading, BorderLayout.CENTER);
        mainPanel.add(topBar, BorderLayout.NORTH);

        // Core dynamic Grid panel form
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 10, 8, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        txtName = createTextFieldRow(formPanel, "Passenger Name:", gbc, 0);
        txtAge = createTextFieldRow(formPanel, "Age:", gbc, 1);
        
        // Gender Row
        gbc.gridy = 2; gbc.gridx = 0; gbc.weightx = 0.3;
        JLabel lblG = new JLabel("Gender:"); lblG.setFont(new Font("Segoe UI", Font.BOLD, 14));
        formPanel.add(lblG, gbc);
        gbc.gridx = 1; gbc.weightx = 0.7;
        cmbGender = new JComboBox<>(new String[]{"Male", "Female", "Other"});
        styleComponent(cmbGender);
        formPanel.add(cmbGender, gbc);

        txtPhone = createTextFieldRow(formPanel, "Phone Number:", gbc, 3);

        // Bus Dropdown Row
        gbc.gridy = 4; gbc.gridx = 0; gbc.weightx = 0.3;
        JLabel lblB = new JLabel("Select Route/Bus:"); lblB.setFont(new Font("Segoe UI", Font.BOLD, 14));
        formPanel.add(lblB, gbc);
        gbc.gridx = 1; gbc.weightx = 0.7;
        cmbBus = new JComboBox<>();
        styleComponent(cmbBus);
        formPanel.add(cmbBus, gbc);

        txtSeat = createTextFieldRow(formPanel, "Assigned Seat #:", gbc, 5);

        loadBuses();
        mainPanel.add(formPanel, BorderLayout.CENTER);

        // Actions Row Layout
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        actionPanel.setOpaque(false);
        
        btnBook = new JButton("Confirm Booking");
        styleButton(btnBook, COLOR_ACCENT, Color.WHITE);
        btnClear = new JButton("Reset");
        styleButton(btnClear, new Color(120, 130, 140), Color.WHITE);

        btnBook.addActionListener(this);
        btnClear.addActionListener(this);
        actionPanel.add(btnBook);
        actionPanel.add(btnClear);
        mainPanel.add(actionPanel, BorderLayout.SOUTH);

        add(mainPanel);
        setVisible(true);
    }

    private JTextField createTextFieldRow(JPanel panel, String text, GridBagConstraints gbc, int row) {
        gbc.gridy = row; gbc.gridx = 0; gbc.weightx = 0.3;
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.BOLD, 14));
        panel.add(label, gbc);
        gbc.gridx = 1; gbc.weightx = 0.7;
        JTextField field = new JTextField();
        styleComponent(field);
        panel.add(field, gbc);
        return field;
    }

    private void styleComponent(JComponent comp) {
        comp.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        comp.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(210, 215, 223), 1),
                BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
    }

    private void styleButton(JButton btn, Color bg, Color fg) {
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setBackground(bg);
        btn.setForeground(fg);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorder(BorderFactory.createEmptyBorder(10, 25, 10, 25));
    }

    private void loadBuses() {
        try {
            Connection con = DBConnection.getConnection();
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("SELECT bus_id, bus_name FROM buses WHERE available_seats > 0");
            while (rs.next()) {
                cmbBus.addItem(rs.getInt("bus_id") + " - " + rs.getString("bus_name"));
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnBook) {
            try {
                if(cmbBus.getSelectedItem() == null) {
                    JOptionPane.showMessageDialog(this, "No operational routes available.");
                    return;
                }
                Connection con = DBConnection.getConnection();
                String passengerSql = "INSERT INTO passengers(name,age,gender,phone) VALUES(?,?,?,?)";
                PreparedStatement ps1 = con.prepareStatement(passengerSql, Statement.RETURN_GENERATED_KEYS);
                ps1.setString(1, txtName.getText());
                ps1.setInt(2, Integer.parseInt(txtAge.getText()));
                ps1.setString(3, cmbGender.getSelectedItem().toString());
                ps1.setString(4, txtPhone.getText());
                ps1.executeUpdate();

                ResultSet generated = ps1.getGeneratedKeys();
                int passengerId = 0;
                if (generated.next()) passengerId = generated.getInt(1);

                String selectedBus = cmbBus.getSelectedItem().toString();
                int busId = Integer.parseInt(selectedBus.split(" - ")[0]);

                double fare = 0;
                PreparedStatement farePs = con.prepareStatement("SELECT fare FROM buses WHERE bus_id=?");
                farePs.setInt(1, busId);
                ResultSet fareRs = farePs.executeQuery();
                if (fareRs.next()) fare = fareRs.getDouble("fare");

                String reservationSql = "INSERT INTO reservations(passenger_id,bus_id,seat_number,booking_date,status,fare) VALUES(?,?,?,?,?,?)";
                PreparedStatement ps2 = con.prepareStatement(reservationSql);
                ps2.setInt(1, passengerId);
                ps2.setInt(2, busId);
                ps2.setInt(3, Integer.parseInt(txtSeat.getText()));
                ps2.setDate(4, Date.valueOf(LocalDate.now()));
                ps2.setString(5, "Booked");
                ps2.setDouble(6, fare);
                ps2.executeUpdate();

                PreparedStatement ps3 = con.prepareStatement("UPDATE buses SET available_seats = available_seats - 1 WHERE bus_id=?");
                ps3.setInt(1, busId);
                ps3.executeUpdate();

                JOptionPane.showMessageDialog(this, "Ticket Booked Successfully!");
                clearForm();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
        if (e.getSource() == btnClear) {
            clearForm();
        }
    }

    private void clearForm() {
        txtName.setText("");
        txtAge.setText("");
        txtPhone.setText("");
        txtSeat.setText("");
        if(cmbGender.getItemCount() > 0) cmbGender.setSelectedIndex(0);
    }
}