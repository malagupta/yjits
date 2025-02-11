package conf.ui;

import conf.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class ConferenceManagementApp extends JFrame {
    private JPanel mainPanel;
    private CardLayout cardLayout;

    // Custom button class for better visual appearance
    private class StyledButton extends JButton {
        private Color normalColor;
        private Color hoverColor;

        public StyledButton(String text, Color color) {
            super(text);
            this.normalColor = color;
            // Create a slightly lighter color for hover effect
            this.hoverColor = new Color(
                Math.min(normalColor.getRed() + 20, 255),
                Math.min(normalColor.getGreen() + 20, 255),
                Math.min(normalColor.getBlue() + 20, 255)
            );

            setBackground(normalColor);
            setForeground(Color.WHITE);
            setFont(new Font("Arial", Font.BOLD, 16));
            setFocusPainted(false);
            setBorderPainted(false);
            setOpaque(true);

            // Add hover effect
            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    setBackground(hoverColor);
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    setBackground(normalColor);
                }
            });
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // Draw rounded rectangle background
            g2.setColor(getBackground());
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);

            super.paintComponent(g2);
            g2.dispose();
        }
    }

    public ConferenceManagementApp() {
        setTitle("Conference Management System");
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                // Allow any cleanup to happen before closing
                dispose();
                System.exit(0);
            }
        });
        setSize(800, 600);
        setLocationRelativeTo(null);

        // Create menu bar
        JMenuBar menuBar = new JMenuBar();
        JMenu manageMenu = new JMenu("Manage");

        // Add menu items
        addMenuItem(manageMenu, "Conferences");
        addMenuItem(manageMenu, "Attendees");
        addMenuItem(manageMenu, "Speakers");
        addMenuItem(manageMenu, "Staff");
        addMenuItem(manageMenu, "Vendors/Sponsors");
        addMenuItem(manageMenu, "Sessions");

        menuBar.add(manageMenu);
        setJMenuBar(menuBar);

        // Main panel with card layout
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        add(mainPanel);

        // Add welcome panel with buttons
        JPanel welcomePanel = new JPanel(new BorderLayout());

        // Add welcome label at the top
        JLabel welcomeLabel = new JLabel("Welcome to Conference Management System", SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 24));
        welcomeLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        welcomePanel.add(welcomeLabel, BorderLayout.NORTH);

        // Create button panel with grid layout
        JPanel buttonPanel = new JPanel(new GridLayout(2, 3, 20, 20));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Define button colors (different shades of blue)
        Color[] buttonColors = {
            new Color(51, 122, 183),   // Bootstrap primary blue
            new Color(0, 150, 255),    // Bright blue
            new Color(30, 144, 255),   // Dodger blue
            new Color(65, 105, 225),   // Royal blue
            new Color(70, 130, 180),   // Steel blue
            new Color(100, 149, 237)   // Cornflower blue
        };

        // Create buttons with consistent size
        String[] buttonLabels = {"Conferences", "Attendees", "Speakers", "Staff", "Vendors/Sponsors", "Sessions"};
        for (int i = 0; i < buttonLabels.length; i++) {
            StyledButton button = new StyledButton(buttonLabels[i], buttonColors[i]);
            button.setPreferredSize(new Dimension(200, 120));

            // Add action listener
            final String label = buttonLabels[i];
            button.addActionListener(e -> switchPanel(label));

            buttonPanel.add(button);
        }

        welcomePanel.add(buttonPanel, BorderLayout.CENTER);
        mainPanel.add(welcomePanel, "Welcome");
        cardLayout.show(mainPanel, "Welcome");
    }

    private void addMenuItem(JMenu menu, String title) {
        JMenuItem menuItem = new JMenuItem(title);
        menuItem.addActionListener(e -> switchPanel(title));
        menu.add(menuItem);
    }

    public void showMainMenu() {
        switchPanel("Welcome");
    }

    void switchPanel(String panelName) {
        JPanel panel;
        switch (panelName) {
            case "Conferences":
                panel = new ConferencePanel();
                break;
            case "Attendees":
                panel = new AttendeePanel();
                break;
            case "Speakers":
                panel = new SpeakerPanel();
                break;
            case "Staff":
                panel = new StaffPanel();
                break;
            case "Vendors/Sponsors":
                panel = new VendorSponsorPanel();
                break;
            case "Sessions":
                panel = new SessionPanel();
                break;
            default:
                panel = new JPanel();
                panel.add(new JLabel("Coming soon: " + panelName + " Management"));
                break;
        }
        mainPanel.removeAll();
        mainPanel.add(panel, panelName);
        mainPanel.revalidate();
        mainPanel.repaint();
        cardLayout.show(mainPanel, panelName);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ConferenceManagementApp app = new ConferenceManagementApp();
            app.setVisible(true);
        });
    }
}
