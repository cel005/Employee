package view;

import Controller.AttendanceController;
import model.Employee;

import javax.swing.*;
import java.awt.*;

public class EmployeeUI extends JFrame {

    private final EmployeeTableModel tableModel;
    private final JTable employeeTable;
    private final AttendanceController controller;

    public EmployeeUI(AttendanceController controller) {
        this.controller = controller;

        setTitle("Employee Records");
        setSize(750, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        tableModel = new EmployeeTableModel();
        employeeTable = new JTable(tableModel);

        // Enable sorting by clicking the table column headings.
        employeeTable.setAutoCreateRowSorter(true);

        JButton refreshButton = new JButton("Refresh");
        refreshButton.addActionListener(e -> loadEmployees());

        setLayout(new BorderLayout(5, 5));
        add(new JScrollPane(employeeTable), BorderLayout.CENTER);
        add(refreshButton, BorderLayout.SOUTH);

        loadEmployees();
    }

    private void loadEmployees() {
        tableModel.setEmployees(controller.getEmployees());
    }
}