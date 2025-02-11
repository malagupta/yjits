package conf.ui;

import conf.Speaker;
import conf.persistence.JsonPersistenceManager;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class SpeakerPanel extends JPanel {
    private JTable speakerTable;
    private DefaultTableModel tableModel;
    private List<Speaker> speakers;
    private JButton addButton;
    private JButton editButton;
    private JButton deleteButton;
    private JsonPersistenceManager persistenceManager;

    // Modern color scheme
    private static final Color BACKGROUND_COLOR = new Color(245, 245, 250);
    private static final Color HEADER_COLOR = new Color(60, 90, 180);
    private static final Color BUTTON_COLOR = new Color(51, 122, 183);
    private static final Color TEXT_COLOR = new Color(50, 50, 50);
    private static final Font HEADER_FONT = new Font("Arial", Font.BOLD, 14);
    private static final Font TABLE_FONT = new Font("Arial", Font.PLAIN, 13);
    private static final Font BUTTON_FONT = new Font("Arial", Font.BOLD, 13);

    public SpeakerPanel() {
        persistenceManager = new JsonPersistenceManager();
        speakers = persistenceManager.loadSpeakers();
        setLayout(new BorderLayout());
        setBackground(BACKGROUND_COLOR);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Create table model with columns
        String[] columns = {"First Name", "Last Name", "Shirt Size", "Unique ID"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        // Create and style table
        speakerTable = new JTable(tableModel);
        speakerTable.setFont(TABLE_FONT);
        speakerTable.setRowHeight(30);
        speakerTable.setIntercellSpacing(new Dimension(10, 5));
        speakerTable.setGridColor(new Color(230, 230, 235));
        speakerTable.getTableHeader().setFont(HEADER_FONT);
        speakerTable.getTableHeader().setBackground(HEADER_COLOR);
        speakerTable.getTableHeader().setForeground(Color.WHITE);
        speakerTable.setSelectionBackground(new Color(210, 220, 240));
        speakerTable.setSelectionForeground(TEXT_COLOR);

        JScrollPane scrollPane = new JScrollPane(speakerTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(HEADER_COLOR, 1));
        add(scrollPane, BorderLayout.CENTER);

        // Populate table with loaded speakers
        for (Speaker speaker : speakers) {
            tableModel.addRow(new Object[]{
                speaker.getFirstName(),
                speaker.getLastName(),
                speaker.getShirtSize(),
                speaker.getUniqueId()
            });
        }

        // Create and style buttons panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(BACKGROUND_COLOR);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 5, 20));
        buttonPanel.setLayout(new GridLayout(1, 3, 15, 0));

        addButton = createStyledButton("Add Speaker", BUTTON_COLOR);
        editButton = createStyledButton("Edit Speaker", BUTTON_COLOR);
        deleteButton = createStyledButton("Delete Speaker", new Color(180, 70, 70));

        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);


        add(buttonPanel, BorderLayout.SOUTH);

        // Add action listeners
        addButton.addActionListener(e -> showSpeakerDialog(null));

        editButton.addActionListener(e -> {
            int selectedRow = speakerTable.getSelectedRow();
            if (selectedRow >= 0) {
                Speaker speaker = speakers.get(selectedRow);
                showSpeakerDialog(speaker);
            } else {
                JOptionPane.showMessageDialog(this, 
                    "Please select a speaker to edit", 
                    "No Selection", 
                    JOptionPane.WARNING_MESSAGE);
            }
        });

        deleteButton.addActionListener(e -> {
            int selectedRow = speakerTable.getSelectedRow();
            if (selectedRow >= 0) {
                int confirm = JOptionPane.showConfirmDialog(this,
                    "Are you sure you want to delete this speaker?",
                    "Confirm Delete",
                    JOptionPane.YES_NO_OPTION);

                if (confirm == JOptionPane.YES_OPTION) {
                    speakers.remove(selectedRow);
                    tableModel.removeRow(selectedRow);
                    persistenceManager.saveSpeakers(speakers);
                }
            } else {
                JOptionPane.showMessageDialog(this,
                    "Please select a speaker to delete",
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

    private void showSpeakerDialog(Speaker speakerToEdit) {
        JDialog dialog = new JDialog((Frame)SwingUtilities.getWindowAncestor(this), 
            speakerToEdit == null ? "Add Speaker" : "Edit Speaker", 
            true);
        dialog.setLayout(new GridBagLayout());
        dialog.getContentPane().setBackground(BACKGROUND_COLOR);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Create form fields with styling
        JTextField firstNameField = createStyledTextField();
        JTextField lastNameField = createStyledTextField();
        JTextField shirtSizeField = createStyledTextField();

        // If editing, populate fields
        if (speakerToEdit != null) {
            firstNameField.setText(speakerToEdit.getFirstName());
            lastNameField.setText(speakerToEdit.getLastName());
            shirtSizeField.setText(speakerToEdit.getShirtSize());
        }

        // Add components to dialog with styled labels
        gbc.gridx = 0; gbc.gridy = 0;
        dialog.add(createStyledLabel("First Name:"), gbc);
        gbc.gridx = 1;
        dialog.add(firstNameField, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        dialog.add(createStyledLabel("Last Name:"), gbc);
        gbc.gridx = 1;
        dialog.add(lastNameField, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        dialog.add(createStyledLabel("Shirt Size:"), gbc);
        gbc.gridx = 1;
        dialog.add(shirtSizeField, gbc);

        // Add buttons with modern styling
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        buttonPanel.setBackground(BACKGROUND_COLOR);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));

        JButton saveButton = createStyledButton("Save", BUTTON_COLOR);
        JButton cancelButton = createStyledButton("Cancel", new Color(150, 150, 150));

        saveButton.addActionListener(e -> {
            try {
                String firstName = firstNameField.getText().trim();
                String lastName = lastNameField.getText().trim();
                String shirtSize = shirtSizeField.getText().trim();

                if (firstName.isEmpty() || lastName.isEmpty() || shirtSize.isEmpty()) {
                    throw new IllegalArgumentException("All fields are required");
                }

                Speaker speaker = new Speaker(firstName, lastName, shirtSize);

                if (speakerToEdit == null) {
                    // Add new speaker
                    speakers.add(speaker);
                    tableModel.addRow(new Object[]{
                        firstName, 
                        lastName, 
                        shirtSize,
                        speaker.getUniqueId()
                    });
                    persistenceManager.saveSpeakers(speakers);
                } else {
                    // Update existing speaker
                    int index = speakers.indexOf(speakerToEdit);
                    speakers.set(index, speaker);
                    int selectedRow = speakerTable.getSelectedRow();
                    tableModel.setValueAt(firstName, selectedRow, 0);
                    tableModel.setValueAt(lastName, selectedRow, 1);
                    tableModel.setValueAt(shirtSize, selectedRow, 2);
                    tableModel.setValueAt(speaker.getUniqueId(), selectedRow, 3);
                    persistenceManager.saveSpeakers(speakers);
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

        // Add button panel to dialog with proper spacing
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);
        dialog.add(buttonPanel, gbc);

        // Final dialog setup
        dialog.pack();
        dialog.setMinimumSize(new Dimension(400, dialog.getHeight()));
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }
}
