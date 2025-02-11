package conf.ui;

import conf.Attendee;
import conf.PaymentType;
import conf.persistence.JsonPersistenceManager;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class AttendeePanel extends JPanel {
    private JTable attendeeTable;
    private DefaultTableModel tableModel;
    private List<Attendee> attendees;
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

    public AttendeePanel() {
        persistenceManager = new JsonPersistenceManager();
        attendees = persistenceManager.loadAttendees();
        setLayout(new BorderLayout());
        setBackground(BACKGROUND_COLOR);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Create table model with columns
        String[] columns = {"First Name", "Last Name", "Payment Type", "Unique ID"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        // Create and style table
        attendeeTable = new JTable(tableModel);
        attendeeTable.setFont(TABLE_FONT);
        attendeeTable.setRowHeight(30);
        attendeeTable.setIntercellSpacing(new Dimension(10, 5));
        attendeeTable.setGridColor(new Color(230, 230, 235));
        attendeeTable.getTableHeader().setFont(HEADER_FONT);
        attendeeTable.getTableHeader().setBackground(HEADER_COLOR);
        attendeeTable.getTableHeader().setForeground(Color.WHITE);
        attendeeTable.setSelectionBackground(new Color(210, 220, 240));
        attendeeTable.setSelectionForeground(TEXT_COLOR);

        JScrollPane scrollPane = new JScrollPane(attendeeTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(HEADER_COLOR, 1));
        add(scrollPane, BorderLayout.CENTER);

        // Initialize table with loaded attendees
        for (Attendee attendee : attendees) {
            tableModel.addRow(new Object[]{
                attendee.getFirstName(),
                attendee.getLastName(),
                attendee.getPaymentType(),
                attendee.getUniqueId()
            });
        }

        // Create and style buttons panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(BACKGROUND_COLOR);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 5, 20));
        buttonPanel.setLayout(new GridLayout(1, 3, 15, 0));

        addButton = createStyledButton("Add Attendee", BUTTON_COLOR);
        editButton = createStyledButton("Edit Attendee", BUTTON_COLOR);
        deleteButton = createStyledButton("Delete Attendee", new Color(180, 70, 70));

        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        add(buttonPanel, BorderLayout.SOUTH);

        // Add action listeners

        addButton.addActionListener(e -> showAttendeeDialog(null));
        editButton.addActionListener(e -> {
            int selectedRow = attendeeTable.getSelectedRow();
            if (selectedRow >= 0) {
                Attendee attendee = attendees.get(selectedRow);
                showAttendeeDialog(attendee);
            } else {
                JOptionPane.showMessageDialog(this, 
                    "Please select an attendee to edit", 
                    "No Selection", 
                    JOptionPane.WARNING_MESSAGE);
            }
        });

        deleteButton.addActionListener(e -> {
            int selectedRow = attendeeTable.getSelectedRow();
            if (selectedRow >= 0) {
                int confirm = JOptionPane.showConfirmDialog(this,
                    "Are you sure you want to delete this attendee?",
                    "Confirm Delete",
                    JOptionPane.YES_NO_OPTION);

                if (confirm == JOptionPane.YES_OPTION) {
                    attendees.remove(selectedRow);
                    tableModel.removeRow(selectedRow);
                    // Save changes to file
                    persistenceManager.saveAttendees(attendees);
                }
            } else {
                JOptionPane.showMessageDialog(this,
                    "Please select an attendee to delete",
                    "No Selection",
                    JOptionPane.WARNING_MESSAGE);
            }
        });
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

    private JButton createStyledButton(String text, Color backgroundColor) {
        StyledButton button = new StyledButton(text, backgroundColor);
        button.setPreferredSize(new Dimension(150, 35));
        return button;
    }

    private void showAttendeeDialog(Attendee attendeeToEdit) {
        JDialog dialog = new JDialog((Frame)SwingUtilities.getWindowAncestor(this), 
            attendeeToEdit == null ? "Add Attendee" : "Edit Attendee", 
            true);
        dialog.setLayout(new GridBagLayout());
        dialog.getContentPane().setBackground(BACKGROUND_COLOR);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Create styled form fields
        JTextField firstNameField = createStyledTextField();
        JTextField lastNameField = createStyledTextField();
        JComboBox<PaymentType> paymentTypeCombo = new JComboBox<>(PaymentType.values());
        paymentTypeCombo.setFont(TABLE_FONT);
        paymentTypeCombo.setBackground(Color.WHITE);

        // If editing, populate fields
        if (attendeeToEdit != null) {
            firstNameField.setText(attendeeToEdit.getFirstName());
            lastNameField.setText(attendeeToEdit.getLastName());
            paymentTypeCombo.setSelectedItem(attendeeToEdit.getPaymentType());
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
        dialog.add(createStyledLabel("Payment Type:"), gbc);
        gbc.gridx = 1;
        dialog.add(paymentTypeCombo, gbc);

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
                PaymentType paymentType = (PaymentType) paymentTypeCombo.getSelectedItem();

                if (firstName.isEmpty() || lastName.isEmpty()) {
                    throw new IllegalArgumentException("First name and last name are required");
                }

                Attendee attendee = new Attendee(firstName, lastName, paymentType);

                if (attendeeToEdit == null) {
                    // Add new attendee
                    attendees.add(attendee);
                    tableModel.addRow(new Object[]{
                        firstName, 
                        lastName, 
                        paymentType,
                        attendee.getUniqueId()
                    });
                } else {
                    // Update existing attendee
                    int index = attendees.indexOf(attendeeToEdit);
                    attendees.set(index, attendee);
                    int selectedRow = attendeeTable.getSelectedRow();
                    tableModel.setValueAt(firstName, selectedRow, 0);
                    tableModel.setValueAt(lastName, selectedRow, 1);
                    tableModel.setValueAt(paymentType, selectedRow, 2);
                    tableModel.setValueAt(attendee.getUniqueId(), selectedRow, 3);
                }

                // Save changes to file
                persistenceManager.saveAttendees(attendees);

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
