package com.busreservation.ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;

public class DashboardFrame extends JFrame {
    
    // Premium Color Palette (Nordic Indigo Tech)
    private final Color COLOR_PRIMARY = new Color(24, 28, 36);       // Deep slate/midnight canvas
    private final Color COLOR_SIDEBAR = new Color(32, 38, 50);       // Rich textured charcoal sidebar
    private final Color COLOR_SECONDARY = new Color(44, 52, 68);     // Interactive default state
    private final Color COLOR_ACCENT = new Color(0, 210, 217);       // Cyan Aurora active accent glow
    private final Color COLOR_ACCENT_GRAD = new Color(0, 150, 200);  // Secondary accent blending
    private final Color COLOR_BG = new Color(243, 246, 250);         // Studio soft background off-white
    private final Color COLOR_CARD_BG = Color.WHITE;                 // Pure card white base
    private final Color COLOR_TEXT_LIGHT = new Color(245, 247, 250); // High contrast light text
    private final Color COLOR_TEXT_MUTED = new Color(138, 149, 168); // Secondary text labels
    private final Color COLOR_TEXT_DARK = new Color(28, 34, 46);     // Strong typographic core
    private final Color COLOR_DANGER = new Color(239, 83, 80);       // Vibrant warning crimson

    private JButton activeButton = null;

    public DashboardFrame() {
        // Window Initialization Settings
        setTitle("Transit Pro Studio — Enterprise Fleet Manager");
        setSize(1100, 680); // Balanced desktop resolution scale
        setMinimumSize(new Dimension(980, 600));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // --- PREMIUM TEXTURED SIDEBAR PANEL ---
        JPanel sidebar = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                // Subtle micro gradient for depth across the sidebar surface
                GradientPaint gp = new GradientPaint(0, 0, COLOR_SIDEBAR, getWidth(), 0, COLOR_PRIMARY);
                g2.setPaint(gp);
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.dispose();
            }
        };
        sidebar.setPreferredSize(new Dimension(280, 680));
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBorder(new EmptyBorder(40, 20, 30, 20));

        // Modern Branding Suite
        JLabel appTitle = new JLabel("TRANSIT PRO");
        appTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        appTitle.setForeground(Color.WHITE);
        appTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel appSubtitle = new JLabel("FLEET ARCHITECTURE STUDIO");
        appSubtitle.setFont(new Font("Segoe UI", Font.BOLD, 10));
        appSubtitle.setForeground(COLOR_ACCENT);
        appSubtitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        sidebar.add(appTitle);
        sidebar.add(Box.createRigidArea(new Dimension(0, 4)));
        sidebar.add(appSubtitle);
        sidebar.add(Box.createRigidArea(new Dimension(0, 45))); // Typographic breathing room

        // Sidebar Action Menu Pipeline
        sidebar.add(createModernNavButton("View Fleet Registry", e -> new ViewBusFrame()));
        sidebar.add(Box.createRigidArea(new Dimension(0, 10)));
        sidebar.add(createModernNavButton("Deploy New Transit", e -> new AddBusFrame()));
        sidebar.add(Box.createRigidArea(new Dimension(0, 10)));
        sidebar.add(createModernNavButton("Issue Passenger Ticket", e -> new BookTicketFrame()));
        sidebar.add(Box.createRigidArea(new Dimension(0, 10)));
        sidebar.add(createModernNavButton("Revoke Reservation", e -> new CancelTicketFrame()));
        sidebar.add(Box.createRigidArea(new Dimension(0, 10)));
        sidebar.add(createModernNavButton("Query Booking Ledger", e -> new SearchBookingFrame()));
        sidebar.add(Box.createRigidArea(new Dimension(0, 10)));
        sidebar.add(createModernNavButton("System Master Records", e -> new ViewBookingsFrame()));

        // Footer layout separation push
        sidebar.add(Box.createVerticalGlue());
        
        JButton btnLogout = createModernNavButton("Secure Exit System", e -> {
            dispose();
            new LoginFrame();
        });
        // Override state for danger action
        btnLogout.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { if(activeButton != btnLogout) btnLogout.setBackground(COLOR_DANGER); }
            public void mouseExited(MouseEvent e) { if(activeButton != btnLogout) btnLogout.setBackground(COLOR_SECONDARY); }
        });
        sidebar.add(btnLogout);

        // --- MAIN WORKSPACE AREA ---
        JPanel workspace = new JPanel();
        workspace.setBackground(COLOR_BG);
        workspace.setLayout(new BorderLayout());
        workspace.setBorder(new EmptyBorder(40, 45, 40, 45));

        // Header Section
        JPanel topHeaderPanel = new JPanel(new BorderLayout());
        topHeaderPanel.setOpaque(false);
        
        JLabel titleMain = new JLabel("Control Dashboard");
        titleMain.setFont(new Font("Segoe UI", Font.BOLD, 30));
        titleMain.setForeground(COLOR_TEXT_DARK);
        
        JLabel subTitleMain = new JLabel("Real-time terminal logistics and reservation metrics routing matrix.");
        subTitleMain.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subTitleMain.setForeground(COLOR_TEXT_MUTED);
        
        topHeaderPanel.add(titleMain, BorderLayout.NORTH);
        topHeaderPanel.add(subTitleMain, BorderLayout.CENTER);
        workspace.add(topHeaderPanel, BorderLayout.NORTH);

        // Grid matrix container for dynamic workspace content card layout
        JPanel contentGrid = new JPanel(new BorderLayout());
        contentGrid.setOpaque(false);
        contentGrid.setBorder(new EmptyBorder(35, 0, 0, 0));

        // Core Interactive Presentation Greeting Card
        JPanel welcomeHeroCard = new ShadowCard(16) {
            @Override
            protected void paintComponent(Graphics g) {
                // Drop shadow layer injection
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                // Premium horizontal gradient fill mapping
                GradientPaint gp = new GradientPaint(0, 0, COLOR_PRIMARY, getWidth(), getHeight(), COLOR_SIDEBAR);
                g2.setPaint(gp);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 16, 16);
                g2.dispose();
            }
        };
        welcomeHeroCard.setLayout(new BorderLayout());
        welcomeHeroCard.setBorder(new EmptyBorder(45, 45, 45, 45));

        JLabel heroHeader = new JLabel("Welcome back Workspace Operator");
        heroHeader.setFont(new Font("Segoe UI", Font.BOLD, 24));
        heroHeader.setForeground(Color.WHITE);
        heroHeader.setBorder(new EmptyBorder(0, 0, 15, 0));
        
        JLabel heroBody = new JLabel("<html>Systems are fully operational. Access peripheral views and update real-time records<br>" +
                "instantly using your secured system terminal access nodes layout panel.<br><br>" +
                "Select a sub-system application utility routing interface path on the left navigation console deck " +
                "to handle automated ledger assignments, query booking manifests, or process vehicle inventory registrations.</html>");
        heroBody.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        heroBody.setForeground(COLOR_TEXT_MUTED);
        
        welcomeHeroCard.add(heroHeader, BorderLayout.NORTH);
        welcomeHeroCard.add(heroBody, BorderLayout.CENTER);
        
        contentGrid.add(welcomeHeroCard, BorderLayout.CENTER);
        workspace.add(contentGrid, BorderLayout.CENTER);

        // Frame Compositing Construction Layout assembly
        add(sidebar, BorderLayout.WEST);
        add(workspace, BorderLayout.CENTER);

        setVisible(true);
    }

    /**
     * Vector Graphics Card Helper Component with custom simulated anti-aliased shadows
     */
    private class ShadowCard extends JPanel {
        private final int radius;
        public ShadowCard(int radius) {
            this.radius = radius;
            setOpaque(false);
        }
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            // Drop shadow projection styling simulation layout
            g2.setColor(new Color(0, 0, 0, 12));
            g2.fillRoundRect(3, 4, getWidth() - 3, getHeight() - 4, radius, radius);
            g2.setColor(COLOR_CARD_BG);
            g2.fillRoundRect(0, 0, getWidth() - 3, getHeight() - 4, radius, radius);
            g2.dispose();
            super.paintComponent(g);
        }
    }

    /**
     * Premium High-Fidelity UI Interactive Button Factory Interface
     */
    private JButton createModernNavButton(String text, ActionListener action) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Active State Indicator strip line styling
                if (this == activeButton) {
                    GradientPaint gp = new GradientPaint(0, 0, COLOR_ACCENT, getWidth(), 0, COLOR_ACCENT_GRAD);
                    g2.setPaint(gp);
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                    
                    // Light active visual glow block tab bar placement indicator
                    g2.setColor(Color.WHITE);
                    g2.fillRoundRect(6, 12, 4, getHeight() - 24, 2, 2);
                } else {
                    g2.setColor(getBackground());
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                }
                g2.dispose();
                super.paintComponent(g);
            }
        };

        // Standard Geometry Initialization Layout bounds variables configurations
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setOpaque(false);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setMaximumSize(new Dimension(250, 48));
        button.setPreferredSize(new Dimension(250, 48));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Premium Typography and text bounds vector alignments inside button surfaces
        button.setFont(new Font("Segoe UI", Font.BOLD, 13));
        button.setForeground(COLOR_TEXT_LIGHT);
        button.setBackground(COLOR_SIDEBAR);
        button.setHorizontalAlignment(SwingConstants.LEFT);
        button.setBorder(new EmptyBorder(0, 24, 0, 0));

        // Advanced Responsive Event Pipeline listeners variables
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (button != activeButton) {
                    button.setBackground(COLOR_SECONDARY);
                    button.setForeground(Color.WHITE);
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (button != activeButton) {
                    button.setBackground(COLOR_SIDEBAR);
                    button.setForeground(COLOR_TEXT_LIGHT);
                }
            }
        });

        button.addActionListener(e -> {
            // Dynamic routing frame update highlight toggle state tracking assignment
            if (activeButton != button && !text.equals("Secure Exit System")) {
                JButton oldButton = activeButton;
                activeButton = button;
                if (oldButton != null) {
                    oldButton.setBackground(COLOR_SIDEBAR);
                    oldButton.setForeground(COLOR_TEXT_LIGHT);
                }
                button.setForeground(Color.WHITE);
            }
            action.actionPerformed(e);
            repaint();
        });

        return button;
    }
}