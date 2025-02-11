package conf.ui;

import conf.Session;
import conf.Speaker;
import conf.persistence.JsonPersistenceManager;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class SessionPanel extends JPanel {
    private JTable sessionTable;
    private DefaultTableModel tableModel;
    private List<Session> sessions;
    private JButton addButton;
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

    public SessionPanel() {
        persistenceManager = new JsonPersistenceManager();
        sessions = persistenceManager.loadSessions();
        setLayout(new BorderLayout());
        setBackground(BACKGROUND_COLOR);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Create table model with columns
        String[] columns = {"Title", "Abstract", "Speaker/Moderator"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        // Create and style table
        sessionTable = new JTable(tableModel);
        sessionTable.setFont(TABLE_FONT);
        sessionTable.setRowHeight(30);
        sessionTable.setIntercellSpacing(new Dimension(10, 5));
        sessionTable.setGridColor(new Color(230, 230, 235));
        sessionTable.getTableHeader().setFont(HEADER_FONT);
        sessionTable.getTableHeader().setBackground(HEADER_COLOR);
        sessionTable.getTableHeader().setForeground(Color.WHITE);
        sessionTable.setSelectionBackground(new Color(210, 220, 240));
        sessionTable.setSelectionForeground(TEXT_COLOR);

        JScrollPane scrollPane = new JScrollPane(sessionTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(HEADER_COLOR, 1));
        add(scrollPane, BorderLayout.CENTER);

        // Initialize table with loaded sessions
        for (Session session : sessions) {
            tableModel.addRow(new Object[]{
                session.getSessionTitle(),
                session.getSessionAbstract(),
                session.getMainSpeakerModerator().getFirstName() + " " + session.getMainSpeakerModerator().getLastName()
            });
        }

        // Create and style buttons panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(BACKGROUND_COLOR);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 5, 20));
        buttonPanel.setLayout(new GridLayout(1, 2, 15, 0));

        addButton = createStyledButton("Add Session", BUTTON_COLOR);
        deleteButton = createStyledButton("Delete Session", new Color(180, 70, 70));

        buttonPanel.add(addButton);
        buttonPanel.add(deleteButton);
        add(buttonPanel, BorderLayout.SOUTH);

        // Add action listeners

        addButton.addActionListener(e -> showSessionDialog());
        deleteButton.addActionListener(e -> {
            int selectedRow = sessionTable.getSelectedRow();
            if (selectedRow >= 0) {
                int confirm = JOptionPane.showConfirmDialog(this,
                    "Are you sure you want to delete this session?",
                    "Confirm Delete",
                    JOptionPane.YES_NO_OPTION);

                if (confirm == JOptionPane.YES_OPTION) {
                    sessions.remove(selectedRow);
                    tableModel.removeRow(selectedRow);
                    // Save changes to file
                    persistenceManager.saveSessions(sessions);
                }
            } else {
                JOptionPane.showMessageDialog(this,
                    "Please select a session to delete",
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

    private JTextArea createStyledTextArea(int rows, int cols) {
        JTextArea textArea = new JTextArea(rows, cols);
        textArea.setFont(new Font("Arial", Font.PLAIN, 14));
        textArea.setBackground(Color.WHITE);
        textArea.setForeground(TEXT_COLOR);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(5, 7, 5, 7)
        ));
        return textArea;
    }

    private JLabel createStyledLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.BOLD, 14));
        label.setForeground(TEXT_COLOR);
        return label;
    }

    private void showSessionDialog() {
        JDialog dialog = new JDialog((Frame)SwingUtilities.getWindowAncestor(this), 
            "Add Session", 
            true);
        dialog.setLayout(new GridBagLayout());
        dialog.getContentPane().setBackground(BACKGROUND_COLOR);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Create styled form fields
        JTextField titleField = createStyledTextField();
        titleField.setColumns(30);
        JTextArea abstractArea = createStyledTextArea(5, 30);
        JScrollPane abstractScrollPane = new JScrollPane(abstractArea);
        abstractScrollPane.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));

        // Speaker fields
        JTextField speakerFirstNameField = createStyledTextField();
        JTextField speakerLastNameField = createStyledTextField();
        JTextField speakerShirtSizeField = createStyledTextField();
        speakerShirtSizeField.setColumns(10);

        // Add components to dialog with styled labels
        gbc.gridx = 0; gbc.gridy = 0;
        dialog.add(createStyledLabel("Title:"), gbc);
        gbc.gridx = 1;
        dialog.add(titleField, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        dialog.add(createStyledLabel("Abstract:"), gbc);
        gbc.gridx = 1;
        dialog.add(abstractScrollPane, gbc);

        // Speaker panel with modern styling
        JPanel speakerPanel = new JPanel(new GridBagLayout());
        speakerPanel.setBackground(BACKGROUND_COLOR);
        speakerPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder(null, "Speaker/Moderator", 
                TitledBorder.DEFAULT_JUSTIFICATION, 
                TitledBorder.DEFAULT_POSITION, 
                HEADER_FONT, 
                TEXT_COLOR),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        GridBagConstraints speakerGbc = new GridBagConstraints();
        speakerGbc.insets = new Insets(5, 5, 5, 5);
        speakerGbc.fill = GridBagConstraints.HORIZONTAL;

        speakerGbc.gridx = 0; speakerGbc.gridy = 0;
        speakerPanel.add(createStyledLabel("First Name:"), speakerGbc);
        speakerGbc.gridx = 1;
        speakerPanel.add(speakerFirstNameField, speakerGbc);

        speakerGbc.gridx = 0; speakerGbc.gridy = 1;
        speakerPanel.add(createStyledLabel("Last Name:"), speakerGbc);
        speakerGbc.gridx = 1;
        speakerPanel.add(speakerLastNameField, speakerGbc);

        speakerGbc.gridx = 0; speakerGbc.gridy = 2;
        speakerPanel.add(createStyledLabel("Shirt Size:"), speakerGbc);
        speakerGbc.gridx = 1;
        speakerPanel.add(speakerShirtSizeField, speakerGbc);

        gbc.gridx = 0; gbc.gridy = 2;
        gbc.gridwidth = 2;
        dialog.add(speakerPanel, gbc);

        // Add buttons with modern styling
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        buttonPanel.setBackground(BACKGROUND_COLOR);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));
        JButton saveButton = createStyledButton("Save", BUTTON_COLOR);
        JButton cancelButton = createStyledButton("Cancel", new Color(150, 150, 150));

        saveButton.addActionListener(e -> {
            try {
                String title = titleField.getText().trim();
                String sessionAbstract = abstractArea.getText().trim();
                String speakerFirstName = speakerFirstNameField.getText().trim();
                String speakerLastName = speakerLastNameField.getText().trim();
                String speakerShirtSize = speakerShirtSizeField.getText().trim();

                if (title.isEmpty() || sessionAbstract.isEmpty() || 
                    speakerFirstName.isEmpty() || speakerLastName.isEmpty() || 
                    speakerShirtSize.isEmpty()) {
                    throw new IllegalArgumentException("All fields are required");
                }

                Speaker speaker = new Speaker(speakerFirstName, speakerLastName, speakerShirtSize);
                Session session = new Session(title, sessionAbstract, speaker);

                sessions.add(session);
                tableModel.addRow(new Object[]{
                    title,
                    sessionAbstract,
                    speakerFirstName + " " + speakerLastName
                });

                // Save changes to file
                persistenceManager.saveSessions(sessions);

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
