package conf.ui;

import conf.Staff;
import conf.persistence.JsonPersistenceManager;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class StaffPanel extends JPanel {
    private JTable staffTable;
    private DefaultTableModel tableModel;
    private List<Staff> staffMembers;
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

    public StaffPanel() {
        persistenceManager = new JsonPersistenceManager();
        staffMembers = persistenceManager.loadStaff();
        setLayout(new BorderLayout());
        setBackground(BACKGROUND_COLOR);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Create table model with columns
        String[] columns = {"First Name", "Last Name", "Hat Size"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        // Create and style table
        staffTable = new JTable(tableModel);
        staffTable.setFont(TABLE_FONT);
        staffTable.setRowHeight(30);
        staffTable.setIntercellSpacing(new Dimension(10, 5));
        staffTable.setGridColor(new Color(230, 230, 235));
        staffTable.getTableHeader().setFont(HEADER_FONT);
        staffTable.getTableHeader().setBackground(HEADER_COLOR);
        staffTable.getTableHeader().setForeground(Color.WHITE);
        staffTable.setSelectionBackground(new Color(210, 220, 240));
        staffTable.setSelectionForeground(TEXT_COLOR);

        JScrollPane scrollPane = new JScrollPane(staffTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(HEADER_COLOR, 1));
        add(scrollPane, BorderLayout.CENTER);

        // Initialize table with loaded staff members
        for (Staff staff : staffMembers) {
            tableModel.addRow(new Object[]{
                staff.getFirstName(),
                staff.getLastName(),
                staff.getHatSize()
            });
        }

        // Create and style buttons panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(BACKGROUND_COLOR);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 5, 20));
        buttonPanel.setLayout(new GridLayout(1, 3, 15, 0));

        addButton = createStyledButton("Add Staff Member", BUTTON_COLOR);
        editButton = createStyledButton("Edit Staff Member", BUTTON_COLOR);
        deleteButton = createStyledButton("Delete Staff Member", new Color(180, 70, 70));

        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        add(buttonPanel, BorderLayout.SOUTH);

        // Add action listeners

        addButton.addActionListener(e -> showStaffDialog(null));
        editButton.addActionListener(e -> {
            int selectedRow = staffTable.getSelectedRow();
            if (selectedRow >= 0) {
                Staff staff = staffMembers.get(selectedRow);
                showStaffDialog(staff);
            } else {
                JOptionPane.showMessageDialog(this, 
                    "Please select a staff member to edit", 
                    "No Selection", 
                    JOptionPane.WARNING_MESSAGE);
            }
        });

        deleteButton.addActionListener(e -> {
            int selectedRow = staffTable.getSelectedRow();
            if (selectedRow >= 0) {
                int confirm = JOptionPane.showConfirmDialog(this,
                    "Are you sure you want to delete this staff member?",
                    "Confirm Delete",
                    JOptionPane.YES_NO_OPTION);

                if (confirm == JOptionPane.YES_OPTION) {
                    staffMembers.remove(selectedRow);
                    tableModel.removeRow(selectedRow);
                    // Save changes to file
                    persistenceManager.saveStaff(staffMembers);
                }
            } else {
                JOptionPane.showMessageDialog(this,
                    "Please select a staff member to delete",
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

    private void showStaffDialog(Staff staffToEdit) {
        JDialog dialog = new JDialog((Frame)SwingUtilities.getWindowAncestor(this), 
            staffToEdit == null ? "Add Staff Member" : "Edit Staff Member", 
            true);
        dialog.setLayout(new GridBagLayout());
        dialog.getContentPane().setBackground(BACKGROUND_COLOR);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Create styled form fields
        JTextField firstNameField = createStyledTextField();
        JTextField lastNameField = createStyledTextField();
        JTextField hatSizeField = createStyledTextField();

        // If editing, populate fields
        if (staffToEdit != null) {
            firstNameField.setText(staffToEdit.getFirstName());
            lastNameField.setText(staffToEdit.getLastName());
            hatSizeField.setText(staffToEdit.getHatSize());
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
        dialog.add(createStyledLabel("Hat Size:"), gbc);
        gbc.gridx = 1;
        dialog.add(hatSizeField, gbc);

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
                String hatSize = hatSizeField.getText().trim();

                if (firstName.isEmpty() || lastName.isEmpty() || hatSize.isEmpty()) {
                    throw new IllegalArgumentException("All fields are required");
                }

                Staff staff = new Staff(firstName, lastName, hatSize);

                if (staffToEdit == null) {
                    // Add new staff member
                    staffMembers.add(staff);
                    tableModel.addRow(new Object[]{
                        firstName, 
                        lastName, 
                        hatSize
                    });
                } else {
                    // Update existing staff member
                    int index = staffMembers.indexOf(staffToEdit);
                    staffMembers.set(index, staff);
                    int selectedRow = staffTable.getSelectedRow();
                    tableModel.setValueAt(firstName, selectedRow, 0);
                    tableModel.setValueAt(lastName, selectedRow, 1);
                    tableModel.setValueAt(hatSize, selectedRow, 2);
                }

                // Save changes to file
                persistenceManager.saveStaff(staffMembers);

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

        gbc.gridx = 0; gbc.gridy = 3;
        gbc.gridwidth = 2;
        dialog.add(buttonPanel, gbc);

        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }
}
