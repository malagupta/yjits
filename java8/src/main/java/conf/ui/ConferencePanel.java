package conf.ui;

import conf.Conference;
import conf.persistence.JsonPersistenceManager;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.time.Year;
import java.util.ArrayList;
import java.util.List;

public class ConferencePanel extends JPanel {
    private JTable conferenceTable;
    private DefaultTableModel tableModel;
    private List<Conference> conferences;
    private JButton addButton;
    private JButton editButton;
    private JButton deleteButton;
    private final JsonPersistenceManager persistenceManager;

    // Modern color scheme
    private static final Color BACKGROUND_COLOR = new Color(245, 245, 250);
    private static final Color HEADER_COLOR = new Color(60, 90, 180);
    private static final Color BUTTON_COLOR = new Color(51, 122, 183);
    private static final Color TEXT_COLOR = new Color(50, 50, 50);
    private static final Font HEADER_FONT = new Font("Arial", Font.BOLD, 14);
    private static final Font TABLE_FONT = new Font("Arial", Font.PLAIN, 13);
    private static final Font BUTTON_FONT = new Font("Arial", Font.BOLD, 13);

    public ConferencePanel() {
        persistenceManager = new JsonPersistenceManager();
        setLayout(new BorderLayout());
        setBackground(BACKGROUND_COLOR);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Create table model with columns
        String[] columns = {"Name", "Nickname", "Year", "Venue"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        // Create and style table
        conferenceTable = new JTable(tableModel);
        conferenceTable.setFont(TABLE_FONT);
        conferenceTable.setRowHeight(30);
        conferenceTable.setIntercellSpacing(new Dimension(10, 5));
        conferenceTable.setGridColor(new Color(230, 230, 235));
        conferenceTable.getTableHeader().setFont(HEADER_FONT);
        conferenceTable.getTableHeader().setBackground(HEADER_COLOR);
        conferenceTable.getTableHeader().setForeground(Color.WHITE);
        conferenceTable.setSelectionBackground(new Color(210, 220, 240));
        conferenceTable.setSelectionForeground(TEXT_COLOR);

        JScrollPane scrollPane = new JScrollPane(conferenceTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(HEADER_COLOR, 1));
        add(scrollPane, BorderLayout.CENTER);

        // Create and style buttons panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(BACKGROUND_COLOR);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 5, 20));
        buttonPanel.setLayout(new GridLayout(1, 3, 15, 0));

        addButton = createStyledButton("Add Conference", BUTTON_COLOR);
        editButton = createStyledButton("Edit Conference", BUTTON_COLOR);
        deleteButton = createStyledButton("Delete Conference", new Color(180, 70, 70));

        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        add(buttonPanel, BorderLayout.SOUTH);

        // Load saved conferences
        conferences = new ArrayList<>();
        Conference savedConference = persistenceManager.loadConference();
        if (savedConference != null) {
            conferences.add(savedConference);
            // Add saved conference to table
            tableModel.addRow(new Object[]{
                savedConference.getName(),
                savedConference.getNickName(),
                savedConference.getYear(),
                savedConference.getVenue()
            });
        }

        // Add window listener to handle application closing
        SwingUtilities.invokeLater(() -> {
            Window window = SwingUtilities.getWindowAncestor(this);
            if (window != null) {
                window.addWindowListener(new WindowAdapter() {
                    @Override
                    public void windowClosing(WindowEvent e) {
                        if (!conferences.isEmpty()) {
                            persistenceManager.saveConference(conferences.get(0));
                        }
                    }
                });
            }
        });

        // Add action listeners

        addButton.addActionListener(e -> showConferenceDialog(null));
        editButton.addActionListener(e -> {
            int selectedRow = conferenceTable.getSelectedRow();
            if (selectedRow >= 0) {
                Conference conference = conferences.get(selectedRow);
                showConferenceDialog(conference);
            } else {
                JOptionPane.showMessageDialog(this, 
                    "Please select a conference to edit", 
                    "No Selection", 
                    JOptionPane.WARNING_MESSAGE);
            }
        });

        deleteButton.addActionListener(e -> {
            int selectedRow = conferenceTable.getSelectedRow();
            if (selectedRow >= 0) {
                int confirm = JOptionPane.showConfirmDialog(this,
                    "Are you sure you want to delete this conference?",
                    "Confirm Delete",
                    JOptionPane.YES_NO_OPTION);

                if (confirm == JOptionPane.YES_OPTION) {
                    conferences.remove(selectedRow);
                    tableModel.removeRow(selectedRow);
                    // Save the updated conference list
                    if (!conferences.isEmpty()) {
                        persistenceManager.saveConference(conferences.get(0));
                    } else {
                        // If no conferences left, create empty file
                        persistenceManager.saveConference(null);
                    }
                }
            } else {
                JOptionPane.showMessageDialog(this,
                    "Please select a conference to delete",
                    "No Selection",
                    JOptionPane.WARNING_MESSAGE);
            }
        });
    }

    private JButton createStyledButton(String text, Color backgroundColor) {
        StyledButton button = new StyledButton(text, backgroundColor);
        button.setPreferredSize(new Dimension(0, 35));  // Only set height, let width be determined by layout
        return button;
    }

    private JTextField createStyledTextField() {
        JTextField textField = new JTextField(20);
        textField.setFont(new Font("Arial", Font.PLAIN, 14));
        textField.setBackground(Color.WHITE);
        textField.setForeground(TEXT_COLOR);
        textField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(5, 7, 5, 7)
        ));
        return textField;
    }

    private JLabel createStyledLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.BOLD, 14));
        label.setForeground(TEXT_COLOR);
        return label;
    }

    private void showConferenceDialog(Conference conferenceToEdit) {
        JDialog dialog = new JDialog((Frame)SwingUtilities.getWindowAncestor(this), 
            conferenceToEdit == null ? "Add Conference" : "Edit Conference", 
            true);
        dialog.setLayout(new GridBagLayout());
        dialog.getContentPane().setBackground(BACKGROUND_COLOR);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Create styled form fields
        JTextField nameField = createStyledTextField();
        JTextField nickNameField = createStyledTextField();
        JTextField yearField = createStyledTextField();
        yearField.setColumns(4);
        JTextField venueField = createStyledTextField();

        // If editing, populate fields
        if (conferenceToEdit != null) {
            nameField.setText(conferenceToEdit.getName());
            nickNameField.setText(conferenceToEdit.getNickName());
            yearField.setText(conferenceToEdit.getYear().toString());
            venueField.setText(conferenceToEdit.getVenue());
        }

        // Add components to dialog with styled labels
        gbc.gridx = 0; gbc.gridy = 0;
        dialog.add(createStyledLabel("Name:"), gbc);
        gbc.gridx = 1;
        dialog.add(nameField, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        dialog.add(createStyledLabel("Nickname:"), gbc);
        gbc.gridx = 1;
        dialog.add(nickNameField, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        dialog.add(createStyledLabel("Year:"), gbc);
        gbc.gridx = 1;
        dialog.add(yearField, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        dialog.add(createStyledLabel("Venue:"), gbc);
        gbc.gridx = 1;
        dialog.add(venueField, gbc);

        // Add buttons with modern styling
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        buttonPanel.setBackground(BACKGROUND_COLOR);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));
        JButton saveButton = createStyledButton("Save", BUTTON_COLOR);
        JButton cancelButton = createStyledButton("Cancel", new Color(150, 150, 150));

        saveButton.addActionListener(e -> {
            try {
                String name = nameField.getText().trim();
                String nickName = nickNameField.getText().trim();
                Year year = Year.parse(yearField.getText().trim());
                String venue = venueField.getText().trim();

                if (name.isEmpty() || nickName.isEmpty() || venue.isEmpty()) {
                    throw new IllegalArgumentException("All fields are required");
                }

                Conference conference = new Conference(name, nickName, year, venue);

                if (conferenceToEdit == null) {
                    // Add new conference
                    conferences.add(conference);
                    tableModel.addRow(new Object[]{name, nickName, year, venue});
                } else {
                    // Update existing conference
                    int index = conferences.indexOf(conferenceToEdit);
                    conferences.set(index, conference);
                    int selectedRow = conferenceTable.getSelectedRow();
                    tableModel.setValueAt(name, selectedRow, 0);
                    tableModel.setValueAt(nickName, selectedRow, 1);
                    tableModel.setValueAt(year, selectedRow, 2);
                    tableModel.setValueAt(venue, selectedRow, 3);
                }

                // Save the updated conference list
                if (!conferences.isEmpty()) {
                    persistenceManager.saveConference(conferences.get(0));
                }

                dialog.dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog,
                    "Invalid input: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            }
        });

        cancelButton.addActionListener(e -> dialog.dispose());

        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);

        gbc.gridx = 0; gbc.gridy = 4;
        gbc.gridwidth = 2;
        dialog.add(buttonPanel, gbc);

        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }
}
