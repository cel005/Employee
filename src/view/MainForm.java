
package view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainForm extends JFrame {

    private JTextField nameField;
    private DefaultTableModel tableModel;
    private JTable table;
    private List<String> names;

    public MainForm() {
        names = new ArrayList<>();

        setTitle("Employee Attendance System");
        setSize(750, 450);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // TITLE
        JLabel title = new JLabel(
                "Employee Attendance System",
                SwingConstants.CENTER
        );
        title.setFont(new Font("Arial", Font.BOLD, 24));
        title.setBorder(BorderFactory.createEmptyBorder(15, 5, 15, 5));
        add(title, BorderLayout.NORTH);

        // INPUT FIELD
        nameField = new JTextField(20);

        JPanel inputPanel = new JPanel(new FlowLayout());
        inputPanel.add(new JLabel("Employee Name:"));
        inputPanel.add(nameField);

        // TABLE
        tableModel = new DefaultTableModel(
                new String[]{"No.", "Employee Name"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table = new JTable(tableModel);
        table.setRowHeight(25);
        table.setSelectionMode(
                ListSelectionModel.SINGLE_SELECTION
        );

        // BUTTONS
        JButton addButton = new JButton("Add Name");
        JButton editButton = new JButton("Edit Name");
        JButton deleteButton = new JButton("Delete Name");
        JButton sortButton = new JButton("Sort A-Z");
        JButton clearButton = new JButton("Clear Input");

        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(sortButton);
        buttonPanel.add(clearButton);

        // LAYOUT
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(inputPanel, BorderLayout.NORTH);
        topPanel.add(buttonPanel, BorderLayout.CENTER);

        add(topPanel, BorderLayout.CENTER);
        add(new JScrollPane(table), BorderLayout.SOUTH);

        // Give the table enough space in the window.
        JPanel mainPanel = new JPanel(new BorderLayout(5, 5));
        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(new JScrollPane(table), BorderLayout.CENTER);

        remove(topPanel);
        add(mainPanel, BorderLayout.CENTER);

        // BUTTON ACTIONS
        addButton.addActionListener(e -> addName());
        editButton.addActionListener(e -> editName());
        deleteButton.addActionListener(e -> deleteName());
        sortButton.addActionListener(e -> sortNames());
        clearButton.addActionListener(e -> nameField.setText(""));

        nameField.addActionListener(e -> addName());

        // Fill the input field when a row is selected.
        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int row = table.getSelectedRow();

                if (row >= 0) {
                    nameField.setText(
                            tableModel.getValueAt(row, 1).toString()
                    );
                }
            }
        });
    }

    // ADD
    private void addName() {
        String name = nameField.getText().trim();

        if (name.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this, "Please enter an employee name."
            );
            return;
        }

        names.add(name);
        nameField.setText("");
        displayNames();
    }

    // EDIT
    private void editName() {
        int row = table.getSelectedRow();

        if (row == -1) {
            JOptionPane.showMessageDialog(
                    this, "Please select an employee to edit."
            );
            return;
        }

        String newName = nameField.getText().trim();

        if (newName.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this, "Please enter the new employee name."
            );
            return;
        }

        names.set(row, newName);
        nameField.setText("");
        displayNames();

        JOptionPane.showMessageDialog(
                this, "Employee name updated successfully."
        );
    }

    // DELETE
    private void deleteName() {
        int row = table.getSelectedRow();

        if (row == -1) {
            JOptionPane.showMessageDialog(
                    this, "Please select an employee to delete."
            );
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to delete "
                + names.get(row) + "?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {
            names.remove(row);
            nameField.setText("");
            displayNames();

            JOptionPane.showMessageDialog(
                    this, "Employee deleted successfully."
            );
        }
    }

    // SORT A-Z
    private void sortNames() {
        Collections.sort(
                names,
                String.CASE_INSENSITIVE_ORDER
        );

        displayNames();
    }

    // DISPLAY TABLE
    private void displayNames() {
        tableModel.setRowCount(0);

        for (int i = 0; i < names.size(); i++) {
            tableModel.addRow(new Object[]{
                i + 1,
                names.get(i)
            });
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new MainForm().setVisible(true);
        });
    }
}
